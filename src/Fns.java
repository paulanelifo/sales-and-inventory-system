
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
import javax.swing.table.TableRowSorter;
public class Fns {
    
    
    //test codes
    
    public static void btnDeleteSelectedRow(JTable table) {
        //change the frmDashboard to whatever the dashboard frame is named
        int selectedRowIndex = table.getSelectedRow();  
        if (selectedRowIndex != -1) {
            int confirmation = JOptionPane.showConfirmDialog(frmdashboard, "Are you sure you want to delete this?");      
            if (confirmation == JOptionPane.YES_OPTION){
                // Get the value of the primary key column (assuming it's the first column)
                Object primaryKeyValue = table.getValueAt(selectedRowIndex, 0);
                // Delete row from the database
                deleteRowFromDatabase(primaryKeyValue);
                // Remove row from JTable
                ((DefaultTableModel) table.getModel()).removeRow(selectedRowIndex);
            }
        }
    }
    public static void deleteRowFromDatabase(Object primaryKeyValue) {
        // Assuming you have a method to establish a connection and execute SQL DELETE statement
        try (
             Statement statement = connection.createStatement()) {
            String deleteQuery = "DELETE FROM products WHERE id = '" + primaryKeyValue + "'";
            statement.executeUpdate(deleteQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void displayRowContent(Object primaryKeyValue) {
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM products WHERE id = '" + primaryKeyValue + "'";
            ResultSet resultSet = statement.executeQuery(query);

            // Assuming you have a method to get column names
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Print row data
            if (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(resultSet.getObject(i) + "\t");
                }
                System.out.println();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void edit(
            JTable table,
            JFrame frame,
            JTextField tfname,
            JButton btncat,
            JTextField tfqty,
            JTextField tfcost,
            JTextField tfsell,
            JTextField tfbrand,
            JTextField tfLST){
        try (Statement statement = connection.createStatement()) {
            Object primaryKeyValue = Fns.getid(table);
            String query = "SELECT * FROM products WHERE id = '" + primaryKeyValue + "'";
            ResultSet resultSet = statement.executeQuery(query);

            // Assuming you have a method to get column names
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Print row data            
            if (resultSet.next()) {
                String srs = "";
                for (int i = 1; i <= columnCount; i++) {
                    //System.out.print(resultSet.getObject(i) + "\t");
                    srs = String.valueOf(resultSet.getObject(i));
                    switch(i){
                        case 2: tfname.setText(srs);break;
                        case 3: btncat.setText(srs);break;
                        case 4: tfqty.setText(srs);break;
                        case 5: tfcost.setText(srs);break;
                        case 6: tfsell.setText(srs);break;
                        case 7: tfbrand.setText(srs);break;
                        case 8: tfLST.setText(srs);break;
                        default: System.out.println("wahapen");break;
                    }
                }
                System.out.println();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void btnEditProd(JFrame frame){
        editing = true;
        
        enableQtyEdit(editing);
        
        dlgAddProd.setLocationRelativeTo(frame);
        dlgAddProd.setVisible(true);
    }
    
    public static void btnUpdateProd(
            JTable table,
            java.awt.Dialog theself,
            JFrame frame,
            JTextField tfname,
            JButton btncat,
            JTextField tfqty,
            JTextField tfcost,
            JTextField tfsell,
            JTextField tfbrand,
            JTextField tfLST
            ){
        Object id = getid(table);
        String sid = String.valueOf(id);
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
            String updateSQL = "UPDATE products SET name = ?, category = ?, qty = ?, cost = ?, sell = ?, brand = ?, LST = ? WHERE id = ?";
            PreparedStatement pps = connection.prepareStatement(updateSQL);
            pps.setString(1, name);
            pps.setString(2, category);
            pps.setString(3, qty);
            pps.setString(4, cost);
            pps.setString(5, sell);
            pps.setString(6, brand);
            pps.setString(7, LST);
            pps.setString(8, sid);
            
            int rowsAffected = pps.executeUpdate();
            pps.close();
            
            JOptionPane.showMessageDialog(theself, "Updated Successfully!");
            theself.dispose();
            
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    //trash codes
    
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
    /*
    public static void btnread(){
        cho.append("he");
        huehue panel = new huehue(cho.toString());
        //pnlProduct.jPanel2.add(createPanel(cho.toString()));
        pnlProduct.jPanel2.add(panel);
        pnlProduct.jPanel2.revalidate();
        pnlProduct.jPanel2.repaint();
    }
    
    */
    
    
    
    
    
    
    
    
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
    public static boolean editing=false;
    private static DefaultTableModel model;  
    public static Object superid=null;
    public static void populateTable(JTable table, String tablename) {
        model = (DefaultTableModel) table.getModel(); // Initialize model

        String query = "SELECT * FROM "+tablename;

        try (
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

        // Make the table not editable
        table.setDefaultEditor(Object.class, null);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }   
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
    public static Object getid(JTable table){
        int selectedRowIndex = table.getSelectedRow();
        if (selectedRowIndex != -1) {
            // Get the value of the selected row
            return table.getValueAt(selectedRowIndex, 0);
        }else{
            return -1;
        }
    }
    
    public static void yez(){
        JOptionPane.showMessageDialog(frmdashboard, "yez");
    }
    public static void naur(){
        JOptionPane.showMessageDialog(frmdashboard, "naur");
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
    public static void btnPanelChangePRODUCT(JPanel panel){
        panel.removeAll();
        //********this pnlProduct is very specific.        
        // Create an instance of pnlProduct
        pnlProduct productPanel = new pnlProduct();

        // Add pnlProduct to jPanel2
        panel.add(productPanel);
        panel.revalidate();
        panel.repaint();
        
        // note that after calling this function, populate the table with the populateTable() function
    }
        
    public static void search(JTextField searchField, JTable table) {
        //use this function right before clearsearch everytime there is changes in database for referesh

        String query = searchField.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
        table.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));
        
    }
    public static void clearsearch(JTextField searchField, JTable table){
        //also serves as refresh
        //use this function right after search everytime there is changes in database for referesh
        searchField.setText("");
        search(searchField,table);
    }
    
    //categoary frame
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
    //end of category frame
    
    //quantity buttons
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
    // end of quantity buttons
    
    //new product
    public static void btnSaveAddProd(java.awt.Dialog theself,
            JFrame frame,
            JTextField tfname,
            JButton btncat,
            JTextField tfqty,
            JTextField tfcost,
            JTextField tfsell,
            JTextField tfbrand,
            JTextField tfLST){
        
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
            PreparedStatement pps = connection.prepareStatement(insertSQL);
            pps.setString(1, id);
            pps.setString(2, name);
            pps.setString(3, category);
            pps.setString(4, qty);
            pps.setString(5, cost);
            pps.setString(6, sell);
            pps.setString(7, brand);
            pps.setString(8, LST);
            
            int rowsAffected = pps.executeUpdate();
            pps.close();
            
            JOptionPane.showMessageDialog(frame, "Saved successfully!");
            btnClear();
            
            theself.dispose();
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        
    }
    public static void btnAddProd(JFrame frame){
        editing = false;
        
        enableQtyEdit(editing);
        
        btnClear();
        
        dlgAddProd.setLocationRelativeTo(frame);
        dlgAddProd.setVisible(true);
    }
    public static void btnClear(){
        dlgAddProd.jTextField1.setText("");
        dlgAddProd.jTextField7.setText("");
        dlgAddProd.jButton1.setText("CATEGORY");
        dlgAddProd.jTextField2.setText("");
        dlgAddProd.jTextField3.setText("");
        dlgAddProd.jTextField4.setText("");
        dlgAddProd.jTextField5.setText("");
    }
    public static void enableQtyEdit(boolean edit){
        dlgAddProd.jTextField2.setEditable(edit);
        dlgAddProd.jButton3.setEnabled(edit);
        dlgAddProd.jButton4.setEnabled(edit);
        dlgAddProd.jButton5.setEnabled(edit);
        dlgAddProd.jButton6.setEnabled(edit);
    }
    //end of new product
    
    //update product
    //end of update product
    
    public static void saveorup(
            boolean edit,
            JTable table,
            java.awt.Dialog theself,
            JFrame frame,
            JTextField tfname,
            JButton btncat,
            JTextField tfqty,
            JTextField tfcost,
            JTextField tfsell,
            JTextField tfbrand,
            JTextField tfLST
            ){
        if(edit){
            btnUpdateProd(table, theself, frame, tfname, btncat, tfqty, tfcost, tfsell, tfbrand, tfLST);
        }
        else if(!edit){
            btnSaveAddProd(theself, frame, tfname, btncat, tfqty, tfcost, tfsell, tfbrand, tfLST);
        }
        
        populateTable(table, "products");
    }
}
