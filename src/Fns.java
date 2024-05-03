
/**
 *
 * @author Pololoers
 */

/*
NOTES:
    - the SQLite database file "myDb.db" should be in root folder
    - UserAuth.java should be accessible.
    - Add this line of code to the main method in the Main class to get it started.
    Fns.home();
    - You can always use 'this' whenever a JFrame is a method argument/parameter.
    - Make sure the components used as method arguments/parameters are 'public' and 'static'.
        to achieve this...
            1. Right click the component.
            2. Select customize code.
            3. Change 'private' to 'public'.
            4. Check the 'static' checkbox.
*/

import java.awt.Dimension;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Random;
public class Fns {
    
    
    //test codes
    private static DefaultTableModel model;    
    public static void populateTable(JTable table, String tablename) {
        model = (DefaultTableModel) table.getModel(); // Initialize model

        String query = "SELECT * FROM "+tablename;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Clear existing data
            model.setRowCount(0);
            model.setColumnCount(0);

            // Get metadata
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Add columns to table model
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            // Add rows to table model
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                model.addRow(rowData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static StringBuilder cho = new StringBuilder();// = new StringBuilder();    
    public static void paul(){
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            // Create a statement
            statement = connection.createStatement();
            // Execute a query to select all rows from the table
            resultSet = statement.executeQuery("SELECT * FROM products");

            // Iterate through the result set and print each row
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                
                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the connection, statement, and result set
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }    
    public static JPanel createPanel(String labelText) {
        JPanel jPanel1 = new JPanel();
        JLabel jLabel1 = new JLabel();

        jPanel1.setBackground(new java.awt.Color(51, 51, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(290, 100));
        jPanel1.setMinimumSize(new java.awt.Dimension(290, 100));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(null, labelText);
            }
        });

        jPanel1.setMaximumSize(new java.awt.Dimension(250, 90));
        jPanel1.setMinimumSize(new java.awt.Dimension(250, 90));
        jPanel1.setPreferredSize(new java.awt.Dimension(250, 90));

        jLabel1.setText(labelText);

        jPanel1.add(jLabel1);

        return jPanel1;
    }
    public static void btnread(){
        cho.append("he");
        huehue panel = new huehue(cho.toString());
        //pnlProduct.jPanel2.add(createPanel(cho.toString()));
        pnlProduct.jPanel2.add(panel);
        pnlProduct.jPanel2.revalidate();
        pnlProduct.jPanel2.repaint();
    }
    
    
    
    
    
    
    
    
    //begin official
    public static void home(){
        //btnLogout();//default
        
        authFrame.dispose();
        frmdashboard.setVisible(true);
        user="admin";
    }
    
    private static UserAuth userAuth = new UserAuth();
    public static Connection getConnection(){
        try {
                // Connect to the SQLite database
                Connection connection = DriverManager.getConnection("jdbc:sqlite:myDb.db");
                return connection;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
    }
    //variables
    public static String user="";
    public static Connection connection = getConnection();
    public static frmDashboard frmdashboard = new frmDashboard(){{            
            setLocationRelativeTo(null);
            dispose();
        }};
    public static frmAuth authFrame = new frmAuth(){{            
            setLocationRelativeTo(null);
            setVisible(true);
        }};
    public static dlgCatSelector dlgCategory = new dlgCatSelector(Fns.frmdashboard,true){{dispose();}};
    public static dlgSaveAddProduct dlgAddProd = new dlgSaveAddProduct(Fns.frmdashboard,true){{dispose();}};

    //convenient coding    
    public static int str2int(String s){
        try{
            return Integer.parseInt(s);
        }catch(NumberFormatException e){
            return 0;
        }
    }
    public static double str2double(String s){
        try{
            return Double.parseDouble(s);
        }catch(NumberFormatException e){
            return 0;
        }
    }
    public static int random5int(){
        Random random = new Random();
        int randomNum = random.nextInt(90000) + 10000;
        return randomNum;
    }
    public static boolean existing(String tblName, String colName, String input){
        try{
            String query = "SELECT * FROM "+tblName+" WHERE "+colName+" = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, input);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public static boolean cannum(String str){
        try {
            // Attempt to convert the string to an integer
            int num = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e1) {
            // If conversion to integer fails, try converting to double
            try {
                double num = Double.parseDouble(str);
                return true;
            } catch (NumberFormatException e2) {
                // If conversion to double also fails, then the string cannot be converted to either int or double
                return false;
            }
        }
    }

    //LOGIN
    private static int registerUser(String name, String pass){
        /*
        1 = success
        2 = if name is too short
        3 = if password is too short
        */
        if (name.length() < 4) {
        return 2; // Username is too short
        }
        if (pass.length() < 8) {
            return 3; // Password is too short
        }
        if (userAuth.usernameExists(name)) {
            return 4; // Username already exists
        }

        // Register user
        if (userAuth.createUser(name, pass)) {
            return 1; // Success
        } else {
            return 0; // Error occurred during registration
        }
    }
    private static boolean isValidLogin(String name, String pass){
        return userAuth.validateLogin(name, pass);
    }
    public static void btnLogin(JTextField username, JPasswordField password, JFrame frm){
        /*
        sample execution in button action:
        Fns.btnLogin(jTextField1.getText(), jPasswordField1.getText(), this);
        */
        if(Fns.isValidLogin(username.getText(), password.getText())){
            JOptionPane.showMessageDialog(frm, "Login success!");
            user = frmAuth.jTextField1.getText();
            username.setText("");
            password.setText("");
            authFrame.dispose();
            frmdashboard.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(frm, "Login failed.");
        }
    }
    public static void btnRegister(JTextField username, JPasswordField password, JFrame frm){
        int reg = registerUser(username.getText(), password.getText());
        switch(reg){
            case 1: JOptionPane.showMessageDialog(frm, "Register success!");
            username.setText("");
            password.setText("");
            break;
            case 2: JOptionPane.showMessageDialog(frm, "Username should be atleast 4 characters.");break;
            case 3: JOptionPane.showMessageDialog(frm, "Password should be atleast 8 characters.");break;
            case 4: JOptionPane.showMessageDialog(frm, "Username already exists.");break;

            default: JOptionPane.showMessageDialog(frm, "bruh");
        }
    }
    public static void btnLogout(){
        user="";
        frmdashboard.dispose();
        authFrame.setLocationRelativeTo(null);
        authFrame.setVisible(true);
    }
    
    //DASHBOARD    
    public static void btnCategory(JButton btn){
        String text = btn.getText();
        if(text.equals("CATEGORY")){
            btn.setText("FOOD AND SNACKS");
            return;
        }
        if(text.equals("FOOD AND SNACKS")){
            btn.setText("BEVERAGES");
            return;
        }
        if(text.equals("BEVERAGES")){
            btn.setText("CANNED GOODS");
            return;
        }
        if(text.equals("CANNED GOODS")){
            btn.setText("RICE AND GRAINS");
            return;
        }
        if(text.equals("RICE AND GRAINS")){
            btn.setText("PERSONAL CARE");
            return;
        }
        if(text.equals("PERSONAL CARE")){
            btn.setText("HOUSEHOLD ITEMS");
            return;
        }
        if(text.equals("HOUSEHOLD ITEMS")){
            btn.setText("COOKING INGREDIENTS");
            return;
        }
        if(text.equals("COOKING INGREDIENTS")){
            btn.setText("SCHOOL SUPPLIES");
            return;
        }
        if(text.equals("SCHOOL SUPPLIES")){
            btn.setText("MISCELLANEOUS");
            return;
        }
        if(text.equals("MISCELLANEOUS")){
            btn.setText("FOOD AND SNACKS");
            return;
        }
        
    }
    public static void btnCatSelector(JFrame frame){        
        dlgCategory.setLocationRelativeTo(frame);
        dlgCategory.setVisible(true);
    }
    public static void catSelectorButns(java.awt.Dialog cat, JButton btn, JButton btninproductpnl){
        String selected = btn.getText();        
        if(!selected.equals("Do not set.")){
            btninproductpnl.setText(selected);
        }else{
            btninproductpnl.setText("CATEGORY");
        }
        cat.setVisible(false);
        cat.dispose();
    }
    public static void btnAdd(JTextField tf){
        try{
            String tftext = tf.getText();
            int value = Integer.parseInt(tftext);
            ++value;
            tf.setText(String.valueOf(value));
        }catch (NumberFormatException ex){
            tf.setText("1");
        }
    }
    public static void btnMinus(JTextField tf){
        try{
            String tftext = tf.getText();
            if(Integer.parseInt(tf.getText())>=1){                
                int value = Integer.parseInt(tftext);
                --value;
                tf.setText(String.valueOf(value));
            }
        }catch (NumberFormatException ex){
            tf.setText("0");
        }
    }
    public static void btnAdd10(JTextField tf){
        try{
            String tftext = tf.getText();
            int value = Integer.parseInt(tftext);
            value+=10;
            tf.setText(String.valueOf(value));
        }catch (NumberFormatException ex){
            tf.setText("10");
        }
    }
    public static void btnMinus10(JTextField tf){
        try{
            String tftext = tf.getText();
            if(Integer.parseInt(tf.getText())>=10){                
                int value = Integer.parseInt(tftext);
                value-=10;
                tf.setText(String.valueOf(value));
                return;
            }
            if(Integer.parseInt(tf.getText())<10){
                tf.setText("0");
            }
        }catch (NumberFormatException ex){
            tf.setText("0");
        }
    }
    public static void btnSaveAddProd(java.awt.Dialog theself, JFrame frame, JTextField tfname,JButton btncat,JTextField tfqty,JTextField tfcost,JTextField tfsell,JTextField tfbrand,JTextField tfLST){
        
        String id=String.valueOf(random5int());
        String name = tfname.getText();
        String category = btncat.getText();if(category.equals("CATEGORY")){category = "";}
        String qty = tfqty.getText();if(!cannum(qty)){qty = "0";}
        String cost = tfcost.getText();if(!cannum(cost)){cost = "0";}
        String sell = tfsell.getText();if(!cannum(sell)){sell = "0";}
        String brand = tfbrand.getText();
        String LST = tfLST.getText();if(!cannum(LST)){LST = "0";}
        
        if(name.equals("")){
            JOptionPane.showMessageDialog(frame, "Please enter product name.");
            return;
        }
        
        try{
            //check if id already exist
            while(existing("products", "id", id)){
                id = String.valueOf(random5int());
            }
            
            String insertSQL = "INSERT INTO products (id, name, category, qty, cost, sell, brand, LST) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, category);
            preparedStatement.setString(4, qty);
            preparedStatement.setString(5, cost);
            preparedStatement.setString(6, sell);
            preparedStatement.setString(7, brand);
            preparedStatement.setString(8, LST);
            
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            
            JOptionPane.showMessageDialog(frame, "Saved successfully!");
            tfname.setText("");
            btncat.setText("CATEGORY");
            tfqty.setText("0");
            tfcost.setText("");
            tfsell.setText("");
            tfbrand.setText("");
            tfLST.setText("");
            
            theself.dispose();
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        
    }
    public static void btnAddProd(JFrame frame){
        dlgAddProd.setLocationRelativeTo(frame);
        dlgAddProd.setVisible(true);
    }
}
