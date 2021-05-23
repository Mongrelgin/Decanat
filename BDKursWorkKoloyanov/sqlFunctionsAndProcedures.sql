--------------------------------------Создание таблицы с пользователями--------------------------------------------------

create table users (id serial, login varchar(30), password varchar(255), role int, primary key(id));

--Процедура добавления пользователя с хешированием пароля с солью
create or replace procedure addUser(login varchar(30), password varchar(255), role int) as $$
declare
    hashPass varchar(255);
begin
    hashPass = crypt(password, gen_salt('md5'));
    insert into users (login, password, role) values(login, hashPass, role);
end;
$$ language plpgsql;

--Аутентификация пользователя
create or replace function checkPassword(_login varchar(30), _password varchar(255)) returns boolean 
as $$ 
declare 
    result boolean; 
begin 
    select (u.password = crypt(_password, u.password)) into result from users u where u.login=_login; 
    return result; 
end; 
$$ language plpgsql;

--------------------------Функции и процедуры для работы с таблицей "Группы"-----------------------------------------------

--Получуние данных из таблицы "Группы"
create or replace function getGroups() returns TABLE(id int, name varchar(20)) as $$
begin
    return query
    SELECT g.id, g.name FROM groups g;
end;
$$ language plpgsql;

--Вставка записи в таблицу "Группы"
create or replace procedure insertGroups(name varchar(20)) as $$
begin
    INSERT INTO groups (name) VALUES(name);
end;
$$ language plpgsql;

--Редактирование записи из таблицы "Группы"
create or replace procedure updateGroups(_name varchar(20), _id int) as $$
begin
    UPDATE groups SET name=_name WHERE groups.id=_id;
end;
$$ language plpgsql;

--Удаление записи из таблицы "Группы"
create or replace procedure deleteGroup(_id int) as $$
begin
    DELETE FROM groups WHERE id=_id;
end;
$$ language plpgsql;

--------------------------Функции и процедуры для работы с таблицей "Люди"-----------------------------------------------

--Получуние данных из таблицы "Люди"
create or replace function getPeople()
returns TABLE(id int, first_name varchar(20), last_name varchar(20), pather_name varchar(20), typpe char(1), group_id int, group_name varchar(20)) as $$
begin
    return query
    SELECT p.id, p.first_name, p.last_name, p.pather_name, p.tipe, g.id, g.name
        FROM people p LEFT JOIN groups g ON p.group_id=g.id;
end;
$$ language plpgsql;

--Вставка записи в таблицу "Люди"
create or replace procedure insertPeople(first_name varchar(20), last_name varchar(20), pather_name varchar(20), group_id int, _tipe varchar(1)) as $$
begin
    INSERT INTO people (first_name, last_name, pather_name, group_id, tipe)
    VALUES(first_name, last_name, pather_name, group_id, _tipe);
end;
$$ language plpgsql;

--Редактирование записи из таблицы "Люди"
create or replace procedure updatePeople(_id int, _first_name varchar(20), _last_name varchar(20), _pather_name varchar(20), _group_id int, _tipe varchar(1)) as $$
begin
    UPDATE people p SET first_name=_first_name, last_name=_last_name, pather_name=_pather_name, group_id=_group_id, tipe=_tipe
    WHERE p.id=_id;
end;
$$ language plpgsql;

--Удаление записи из таблицы "Люди"
create or replace procedure deletePeople(_id int) as $$
begin
    DELETE FROM people p WHERE p.id=_id;
end;
$$ language plpgsql;

--------------------------Функции и процедуры для работы с таблицей "Оценки"-----------------------------------------------

--Получуние данных из таблицы "Оценки"
create or replace function getMarks()
returns TABLE(id int, student varchar(20), subject varchar(50), teacher varchar(20) , _value int) as $$
begin
    return query
    SELECT m.id, pp.last_name, s.name, ps.last_name, m.value FROM marks m
        JOIN subjects s ON s.id=m.subject_id
        JOIN people pp ON pp.id=m.student_id
        JOIN people ps ON ps.id=m.teacher_id;
end;
$$ language plpgsql;

--Вставка записи в таблицу "Оценки"
create or replace procedure insertMarks(_name varchar(200), _cost numeric(18,2), _department_id int, _date_beg date, _date_end date) as $$
begin
    INSERT INTO projects (name, cost, department_id, date_beg, date_end) 
    VALUES (_name, _cost, _department_id, _date_beg, _date_end); 
end;
$$ language plpgsql;

--Редактирование записи из таблицы "Оценки"
create or replace procedure updateProject(_id int, _name varchar(200), _cost numeric(18,2), _department_id int, _date_beg date, _date_end date, _date_end_real date) as $$
begin
    UPDATE projects p SET name=_name, cost=_cost, department_id=_department_id, date_beg=_date_beg, date_end=_date_end, date_end_real=_date_end_real WHERE p.id=_id;
end;
$$ language plpgsql;

--Удаление записи из таблицы "Оценки"
create or replace procedure deleteProject(_id int) as $$
begin
    DELETE FROM projects p WHERE p.id=_id; 
end;
$$ language plpgsql;

-------------------------------------Формирование отчета-------------------------------------------------------------------------------

--Хранимая процедура для расчета суммы прибыли от завершенных к настоящему времени проектов за период
/*
Необходимо реализовать хранимую процедуру, рассчитывающую сумму прибыли, полученную фирмой за некоторый период. 
Хранимая процедура должна иметь один входной параметр, задающий время, с которого будем считать доход и один выходной, 
в котором возвращать размер прибыли.
Предлагаемый алгоритм: создаем курсор, который пробегает по проектам, реальная дата завершения которых меньше текущего времени, 
но больше дате из входного параметра. 
Для каждой строки рассчитываем сумму прибыли: вычисляем сколько было потрачено на проект и вычитаем эту сумму из суммы стоимости проекта. 
Суммируем полученный результат в некоторой переменной, значение которой по окончании работы курсора будет выдано в качестве выходного параметра.
*/
CREATE OR REPLACE FUNCTION profitAt(timeStart DATE) RETURNS NUMERIC(18,2)
AS $$
DECLARE
    cur CURSOR (_key DATE) FOR SELECT p.cost, SUM(e.salary) sal FROM projects p 
    JOIN department_employees de ON de.department_id=p.department_id 
    JOIN employees e ON e.id=de.employee_id 
	WHERE p.date_end_real < CURRENT_DATE AND p.date_end_real > _key GROUP BY p.id;
    costProject NUMERIC(18,2);
    spending NUMERIC(18,2);
    totalCost NUMERIC(18,2);
BEGIN
    OPEN cur(timeStart);
    totalCost=0;
    LOOP
        FETCH cur INTO costProject, spending;
        IF NOT FOUND THEN EXIT; END IF; 
        totalCost=totalCost+(costProject-spending);
    END LOOP;
    RETURN totalCost;
END;
$$ LANGUAGE plpgsql;

SELECT * FROM profitAt('2020-04-01');

--Возвращает таблицу для курсора расчета прибыли за период
create or replace function profitAtTable(timeStart DATE) 
returns TABLE(_name varchar(200), _cost numeric(18,2), _date_beg date, _date_end date, _date_end_real date) as $$
begin
    return query
    SELECT p.name, p.cost, p.date_beg, p.date_end, p.date_end_real 
    FROM projects p JOIN department_employees de ON de.department_id=p.department_id 
    JOIN employees e ON e.id=de.employee_id
    WHERE p.date_end_real < CURRENT_DATE AND p.date_end_real > timeStart GROUP BY p.id;
end;
$$ language plpgsql;

--Функция рассчета предполагаемой прибыли
create or replace function futureProfit()
returns TABLE(_name varchar(200), _cost numeric(18,2), _date_beg date, _date_end date, _profit numeric(18,2)) as $$
begin
    return query
    SELECT p.name, p.cost, p.date_beg, p.date_end, p.cost-(SUM(e.salary)*((p.date_end-p.date_beg)/30)) profit
    FROM projects p JOIN department_employees de ON de.department_id=p.department_id 
    JOIN employees e ON e.id=de.employee_id 
    WHERE p.date_end_real IS NULL GROUP BY p.name, p.cost, p.date_end, p.date_beg;
end;
$$ language plpgsql;