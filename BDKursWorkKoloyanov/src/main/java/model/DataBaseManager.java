package model;

import java.sql.*;
import java.util.ArrayList;
import model.*;

public class DataBaseManager {
    private static DataBaseManager shared = new DataBaseManager();
    private static String url;
    private static String username;
    private static String password;
    private static Connection connection = null;
    private static User user = null;

    public static DataBaseManager getShared() {
        if(shared == null) {
            shared = new DataBaseManager();
        }

        return shared;
    }

    private DataBaseManager() {}

    public void setConfiguration(String url, String username, String password) {
        DataBaseManager.url = url;
        DataBaseManager.username = username;
        DataBaseManager.password = password;
    }

    public User getUser() { return user; }

    public void connect() throws SQLException, ClassNotFoundException {
        System.out.println("Тестирование подключения к базе данных:");
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(url, username, password);
    }

    public void disconnect() throws SQLException { connection.close(); }

    public boolean auth(String login, String password) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = connection.prepareStatement("SELECT checkPassword(?,?);");
        statement.setString(1,login);
        statement.setString(2,password);

        try(ResultSet request = statement.executeQuery()) {
            request.next();
            return request.getBoolean(1);
        }
    }

    public void loadUser(String login) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = connection.prepareStatement("SELECT u.id, u.login, u.role FROM users u WHERE u.login=?;");
        statement.setString(1, login);

        try (ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            user = new User(resultSet.getInt(1), resultSet.getString(2), UserRole.values()[resultSet.getInt(3)]);
        }
    }

    public ArrayList<People> getPeople() throws SQLException, ClassNotFoundException {
        connect();
        ArrayList<People> array = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT id, first_name, last_name, pather_name, typpe, group_id, group_name FROM getPeople();");
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Group group = null;
                if (resultSet.getInt(6) != Types.NULL)
                    group = new Group(resultSet.getInt(6), resultSet.getString(7));
                array.add(new People(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getString(5).charAt(0), group));
            }
        }
        return array;
    }

    public void insertPeople(People people) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = connection.prepareStatement("CALL insertPeople(?, ?, ?, ?, ?);");
        statement.setString(1, people.getName());
        statement.setString(2, people.getLastName());
        statement.setString(3, people.getPartherName());
        if (people.getGroup() == null) {
            statement.setNull(4, Types.NULL);
        } else {
            statement.setInt(4, people.getGroup().getId());
        }
        if (people.getType() == null || people.getType().equals("")) {
            statement.setNull(5, Types.NULL);
        } else {
            statement.setString(5, people.getType().toString());
        }
        statement.executeUpdate();
    }

    public void updatePeople(People people) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = connection.prepareStatement("CALL updatePeople(?, ?, ?, ?, ?, ?);");
        statement.setInt(1, people.getId());
        statement.setString(2, people.getName());
        statement.setString(3, people.getLastName());
        statement.setString(4, people.getPartherName());

        if (people.getGroup() == null) {
            statement.setNull(5, Types.NULL);
        } else {
            statement.setInt(5, people.getGroup().getId());
            System.out.println(people.getGroup().getId());
        }

        if (people.getType() == null) {
            statement.setNull(6, Types.NULL);
        } else {
            statement.setString(6, people.getType().toString());
        }
        statement.executeUpdate();
    }

    public void deletePeople(People people) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = connection.prepareStatement("CALL deletePeople(?)");
        statement.setInt(1, people.getId());
        statement.executeUpdate();
    }

    public ArrayList<Marks> getProjects() throws SQLException, ClassNotFoundException {
        ArrayList<Marks> array = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT id, student, subject, teacher, _value FROM getmarks();");
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                array.add(new Marks(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5)));
            }
        }
        return array;
    }

    public void insertProject(Marks marks) throws SQLException, ClassNotFoundException {
        /*
        PreparedStatement statement = connection.prepareStatement("CALL insertProject(?, ?, ?, ?, ?)");
        statement.setString(1, marks.getName());
        if (marks.getPrice() == null) {
            statement.setNull(2, Types.NULL);
        } else {
            statement.setInt(2, marks.getPrice());
        }
        if (marks.getDepartment() == null) {
            statement.setNull(3, Types.NULL);
        } else {
            statement.setInt(3, marks.getDepartment().getId());
        }
        if (marks.getDateBeg() == null) {
            statement.setNull(4, Types.NULL);
        } else {
            statement.setDate(4, marks.getDateBeg());
        }
        if (marks.getDateEnd() == null) {
            statement.setNull(4, Types.NULL);
        } else {
            statement.setDate(4, marks.getDateEnd());
        }
        statement.executeUpdate();

         */
    }

    public void updateProject(Marks marks) throws SQLException, ClassNotFoundException {
        /*
        PreparedStatement statement = connection.prepareStatement("CALL updateProject(?, ?, ?, ?, ?, ?, ?)");
        statement.setInt(1, marks.getId());
        statement.setString(2, marks.getName());
        if (marks.getPrice() == null) {
            statement.setNull(3, Types.NULL);
        } else {
            statement.setInt(3, marks.getPrice());
        }
        if (marks.getDepartment() == null) {
            statement.setNull(4, Types.NULL);
        } else {
            statement.setInt(4, marks.getDepartment().getId());
        }
        if (marks.getDateBeg() == null) {
            statement.setNull(5, Types.NULL);
        } else {
            statement.setDate(5, marks.getDateBeg());
        }
        if (marks.getDateEnd() == null) {
            statement.setNull(6, Types.NULL);
        } else {
            statement.setDate(6, marks.getDateEnd());
        }
        if (marks.getDateEndReal() == null) {
            statement.setNull(7, Types.NULL);
        } else {
            statement.setDate(7, marks.getDateEndReal());
        }
        statement.executeUpdate();

         */
    }

    public void deleteProject(Marks marks) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = connection.prepareStatement("CALL deleteProject(?);");
        statement.setInt(1, marks.getId());
        statement.executeUpdate();
    }

    //getGroups
    public ArrayList<Group> getGroups() throws SQLException, ClassNotFoundException {
        ArrayList<Group> array = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT id, name FROM getGroups();");

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                array.add(new Group(resultSet.getInt(1), resultSet.getString(2)));
            }
        }

        return array;
    }
    //insertToGroups
    public void insertToGroups(String name) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = connection.prepareStatement("CALL insertGroups(?);");
        statement.setString(1, name);
        statement.executeUpdate();
    }
    //updateGroups
    public void updateGroups(Group group) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = connection.prepareStatement("CALL updateGroups(?, ?);");
        statement.setString(1, group.getName());
        statement.setInt(2, group.getId());
        statement.executeUpdate();
    }
    //deleteFromGroups
    public void deleteFromGroups(Group group) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = connection.prepareStatement("CALL deleteGroup(?);");
        statement.setInt(1, group.getId());
        statement.executeUpdate();
    }

    //getGroups
    public ArrayList<Subject> getSubjects() throws SQLException, ClassNotFoundException {
        ArrayList<Subject> array = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT id, name FROM getSubjects();");

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                array.add(new Subject(resultSet.getInt(1), resultSet.getString(2)));
            }
        }

        return array;
    }
    //insertToGroups
    public void insertToSubjects(String name) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = connection.prepareStatement("CALL insertSubjects(?);");
        statement.setString(1, name);
        statement.executeUpdate();
    }
    //updateGroups
    public void updateSubjects(Subject subject) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = connection.prepareStatement("CALL updateSubjects(?, ?);");
        statement.setString(1, subject.getName());
        statement.setInt(2, subject.getId());
        statement.executeUpdate();
    }
    //deleteFromGroups
    public void deleteFromSubjects(Subject subject) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = connection.prepareStatement("CALL deleteSubjects(?);");
        statement.setInt(1, subject.getId());
        statement.executeUpdate();
    }

    public ArrayList<Report> getReportProfitAtTable(Date date) throws SQLException {
        ArrayList<Report> reportArray = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT _name, _cost, _date_beg, _date_end, _date_end_real FROM profitAtTable(?);");
        statement.setDate(1, date);
        try(ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                reportArray.add(new Report(resultSet.getString(1), resultSet.getInt(2),
                        resultSet.getDate(3), resultSet.getDate(4), resultSet.getDate(5)));
            }
        }

        return reportArray;
    }

    public Integer getReportProfitAt(Date date) throws SQLException {
        Integer profit = null;

        PreparedStatement statement = connection.prepareStatement("SELECT profitat FROM profitAt(?);");
        statement.setDate(1, date);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                profit = resultSet.getInt(1);
            }
        }

        return profit;
    }

    public ArrayList<Report> getReportFutureProfit() throws SQLException {
        ArrayList<Report> reports = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT _name, _cost, _date_beg, _date_end, _profit FROM futureProfit();");

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                reports.add(new Report(resultSet.getString(1), resultSet.getInt(2),
                        resultSet.getDate(3), resultSet.getDate(4), resultSet.getInt(5)));
            }
        }

        return reports;
    }

}
