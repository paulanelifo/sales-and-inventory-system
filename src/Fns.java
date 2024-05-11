
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
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Random;
import javax.swing.table.TableRowSorter;
public class Fns {
    
    
    //test codes
    


    
    
    
    
    
    
    
    
    
    //trash codes
    
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
    
    
    
    
    
    
    //begin official
    public static void home(){
        
        authFrame.dispose();
        frmdashboard.setVisible(true);
        user="admin";
        
        //btnLogout();//default
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
    public static JButton theChosenButton;
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
    public static dlgCatSelector dlgCategory = new dlgCatSelector(frmdashboard,true){{dispose();}};
    public static dlgSaveAddProduct dlgAddProd = new dlgSaveAddProduct(frmdashboard,true){{dispose();}};
    public static dlgResupply dlgResupplyy = new dlgResupply(frmdashboard, true){{dispose();}};
    public static dlgMonthPicker dlgMoPicker = new dlgMonthPicker(frmdashboard,true){{dispose();}};
    public static dlgTransactCash dlgtransactcash = new dlgTransactCash(frmdashboard, true){{dispose();}};
    
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
            if(tablename.equals("products")){
                for (int i = 1; i <= columnCount; i++) {
                    //model.addColumn(metaData.getColumnName(i));
                    switch(i){
                        case 1:model.addColumn("ID");break;
                        case 2:model.addColumn("Name");break;
                        case 3:model.addColumn("Category");break;
                        case 4:model.addColumn("Stock");break;
                        case 5:model.addColumn("Cost");break;
                        case 6:model.addColumn("Retail Price");break;
                        case 7:model.addColumn("Brand");break;
                        case 8:model.addColumn("Low Stock Threshold");break;
                        default: model.addColumn(metaData.getColumnName(i));
                    }
                }
            }
            else if(tablename.equals("money")){
                for (int i = 1; i <= columnCount; i++) {
                    //model.addColumn(metaData.getColumnName(i));
                    switch(i){
                        case 1:model.addColumn("Transaction ID");break;
                        case 2:model.addColumn("Date");break;
                        case 3:model.addColumn("Amount");break;
                        case 4:model.addColumn("Description");break;
                        default:model.addColumn(metaData.getColumnName(i));break;
                    }
                }
            }
            else{
               for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnName(i));
               }
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
    public static int random6int() {
        Random random = new Random();
        int randomNum = random.nextInt(900000) + 100000;
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
    public static String moneyComma(Number number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }
    public static String translateDateString(String dateString) {
        String monthName = "";
        String day = dateString.substring(2, 4);
        String year = dateString.substring(4);

        switch (dateString.substring(0, 2)) {
            case "01": monthName = "January"; break;
            case "02": monthName = "February"; break;
            case "03": monthName = "March"; break;
            case "04": monthName = "April"; break;
            case "05": monthName = "May"; break;
            case "06": monthName = "June"; break;
            case "07": monthName = "July"; break;
            case "08": monthName = "August"; break;
            case "09": monthName = "September"; break;
            case "10": monthName = "October"; break;
            case "11": monthName = "November"; break;
            case "12": monthName = "December"; break;
            default: break;
        }

        return monthName + " " + day + ", " + year;
    }
    public static int intforcezero(String s){
        try{
            return Integer.parseInt(s);
        }catch(NumberFormatException e){
            return 0;
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
    public static void tableClick(
            JTable table,
            JLabel lblid,
            JLabel lblname,
            JLabel lblbrand,
            JLabel lblcategory,
            JLabel lblqty,
            JLabel lblcost,
            JLabel lblsell,
            JLabel lblLST){
        if(table.getSelectedRow() != -1){
            try (Statement statement = connection.createStatement()) {
                Object primaryKeyValue = getid(table);
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
                            case 1: lblid.setText(srs);break;
                            case 2: lblname.setText(srs);break;
                            case 3: lblcategory.setText(srs);break;
                            case 4: lblqty.setText(srs);break;
                            case 5: lblcost.setText(srs);break;
                            case 6: lblsell.setText(srs);break;
                            case 7: lblbrand.setText(srs);break;
                            case 8: lblLST.setText(srs);break;
                            default: break;
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            
        }
        

    }    
    
    //delete product
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
    //end of delete product
    
    public static void search(JTextField searchField, JTable table) {
        //use this function right before clearsearch everytime there is changes in database for referesh

        String query = searchField.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
        table.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));
        
    }
    public static void clearsearch(JTextField searchField, JTable table,
            JLabel lbl2,
            JLabel lbl4,
            JLabel lbl6,
            JLabel lbl8,
            JLabel lbl10,
            JLabel lbl12,
            JLabel lbl14,
            JLabel lbl16
    ){
        //also serves as refresh
        //use this function right after search everytime there is changes in database for referesh
        searchField.setText("");
        search(searchField,table);
        lbl2.setText("");
        lbl4.setText("");
        lbl6.setText("");
        lbl8.setText("");
        lbl10.setText("");
        lbl12.setText("");
        lbl14.setText("");
        lbl16.setText("");
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
            long value = Long.parseLong(tftext);
            ++value;
            tf.setText(String.valueOf(value));
        }catch (NumberFormatException ex){
            tf.setText("1");
        }
    }
    public static void btnMinus(JTextField tf){
        try{
            String tftext = tf.getText();
            if(Long.parseLong(tf.getText())>=1){                
                long value = Long.parseLong(tftext);
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
            long value = Long.parseLong(tftext);
            value+=10;
            tf.setText(String.valueOf(value));
        }catch (NumberFormatException ex){
            tf.setText("10");
        }
    }
    public static void btnMinus10(JTextField tf){
        try{
            String tftext = tf.getText();
            if(Long.parseLong(tf.getText())>=10){                
                long value = Long.parseLong(tftext);
                value-=10;
                tf.setText(String.valueOf(value));
                return;
            }
            if(Long.parseLong(tf.getText())<10){
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
        String day = dlgSaveAddProduct.jTextField6.getText();
        String yr = dlgSaveAddProduct.jTextField8.getText();
        if(!cannum(day)||!cannum(yr)){
            JOptionPane.showMessageDialog(frmdashboard, "Please confirm date.");
            return;
        }
        
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
            try{
                String aid = receiptIdSaveProd();
                String adate = receiptDateSaveProd();
                String aamount = receiptAmountSaveProd();
                String adesc = receiptDescriptionSaveProd();
                insertSQL = "INSERT INTO money (id, date, amount, desc) VALUES (?, ?, ?, ?)";
                pps = connection.prepareStatement(insertSQL);
                pps.setString(1, aid);
                pps.setString(2, adate);
                pps.setString(3, aamount);
                pps.setString(4, adesc);

                rowsAffected = pps.executeUpdate();
                pps.close();

                //JOptionPane.showMessageDialog(frmdashboard, "Saved successfully!");

                dlgtransactcash.dispose();

                dlgSaveAddProduct.jTextField6.setText("1");
                dlgSaveAddProduct.jTextField8.setText("2024");
                //tfamount.setText("");
                //ta.setText("");

            }catch(SQLException e){
                e.printStackTrace();
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(frmdashboard, "Please ensure number input.");
            }
            
            
            JOptionPane.showMessageDialog(frame, "Saved successfully!");
            btnClear();
            
            theself.dispose();
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        
    }
    public static void btnAddProd(JFrame frame){
        editing = false;
        
        enableQtyEdit(!editing);
        
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
            Object primaryKeyValue = getid(table);
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
    public static void btnEditProd(JFrame frame, JTable table){
        if(getid(table).equals(-1)){
            return;
        }
        editing = true;
        
        enableQtyEdit(!editing);
        
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
    //end of update product
    
    //Resupply    
    public static void btnResupply(JFrame frame, JTable table,
            JLabel lblid,
            JLabel lblname,
            JLabel lblbrand,
            JLabel lblcategory,
            JLabel lblqty,
            JLabel lblcost,
            JLabel lblsell,
            JLabel lblLST
            ){
        if(table.getSelectedRow()==-1){
            return;
        }
        
        tableClick(table, lblid, lblname, lblbrand, lblcategory, lblqty, lblcost, lblsell, lblLST);
        
        dlgResupplyy.setLocationRelativeTo(frame);
        dlgResupplyy.setVisible(true);
    }
    public static void totalCostCompute(JLabel lblcost, JTextField tf, JLabel lbltotal, JLabel lblqty, JLabel lblsap){
        long cost,tobuy,total;
        try{
            cost = Long.parseLong(lblcost.getText());
            tobuy = Long.parseLong(tf.getText());
            if(tf.getText().isEmpty()){
                tobuy=0;
            }
            total = cost*tobuy;
            lbltotal.setText(String.valueOf(total));
            
            newSupplyCompute(lblqty, tf, lblsap);
            
        }catch(NumberFormatException e){
        }
        /*
Fns.totalCostCompute(jLabel12, jTextField1, jLabel19);
        */
    }
    public static void newSupplyCompute(JLabel lblqty, JTextField tf, JLabel lblsap){
        long qty, tobuy, sap;
        try{
            qty = Long.parseLong(lblqty.getText());
            tobuy = Long.parseLong(tf.getText());
            if(tf.getText().isEmpty()){
                tobuy=0;
            }
            sap = qty+tobuy;
            lblsap.setText(String.valueOf(sap));
        }catch(NumberFormatException e){            
        }
    }
    public static void btnUpdResupply(java.awt.Dialog theself, JLabel lblqty, JLabel lblid){
        try{
            String qty = lblqty.getText();
            String id = lblid.getText();
            if(!cannum(qty)){
                JOptionPane.showMessageDialog(frmdashboard,"Please ensure number input.");
            }
            String updateSQL = "UPDATE products SET qty = ? WHERE id = ?";
            PreparedStatement pps = Fns.connection.prepareStatement(updateSQL);
            pps.setString(1, qty);
            pps.setString(2, id);
            
            int rowsAffected = pps.executeUpdate();
            pps.close();
            
            JOptionPane.showMessageDialog(theself, "Updated Successfully!");
            theself.dispose();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
//end of resupply
    
    //pnlReport
    public static long totalCapital(){
        try(Statement statement = Fns.connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT amount FROM money");
            long mony=0;
            while (resultSet.next()) {
                // Read the value of the specific column
                String columnValue = resultSet.getString("amount");
                mony+=Integer.parseInt(columnValue);
            }
            return mony;
        }catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }
    public static void updateCapital(){
        pnlReport.jLabel2.setText("P"+String.valueOf(moneyComma(totalCapital())));
    }
    public static void btnPanelChangeREPORT(JPanel panel){
        panel.removeAll();
        //********this pnlProduct is very specific.        
        // Create an instance of pnlProduct
        pnlReport reportpanel = new pnlReport();
        
        Fns.populateTable(pnlReport.jTable1, "money");
        //pnlReport.jLabel2.setText("P"+String.valueOf(moneyComma(totalCapital())));
        updateCapital();
        
        // Add pnlProduct to jPanel2
        panel.add(reportpanel);
        panel.revalidate();
        panel.repaint();
        
        // note that after calling this function, populate the table with the populateTable() function
    }
    public static void btnMonthh(JFrame frame){
        dlgMoPicker.setLocationRelativeTo(frame);
        dlgMoPicker.setVisible(true);
    }
    public static void btnChosenMonth(JButton dlg, JButton chosen){
        String s = dlg.getText();
        chosen.setText(s);
        Fns.dlgMoPicker.dispose();
    }
    public static void btnChooseMonthDown(JButton btnmo){
        String mo = btnmo.getText();
        switch (mo){
            case "January":btnmo.setText("February");break;
            case "February":btnmo.setText("March");break;
            case "March":btnmo.setText("April");break;
            case "April":btnmo.setText("May");break;
            case "May":btnmo.setText("June");break;
            case "June":btnmo.setText("July");break;
            case "July":btnmo.setText("August");break;
            case "August":btnmo.setText("September");break;
            case "September":btnmo.setText("October");break;
            case "October":btnmo.setText("November");break;
            case "November":btnmo.setText("December");break;
            case "December":btnmo.setText("January");break;
            default:btnmo.setText("January");break;
        }
    }
    public static void btnChooseMonthUp(JButton btnmo){
        String mo = btnmo.getText();
        switch (mo){
            case "January": btnmo.setText("December"); break;
            case "February": btnmo.setText("January"); break;
            case "March": btnmo.setText("February"); break;
            case "April": btnmo.setText("March"); break;
            case "May": btnmo.setText("April"); break;
            case "June": btnmo.setText("May"); break;
            case "July": btnmo.setText("June"); break;
            case "August": btnmo.setText("July"); break;
            case "September": btnmo.setText("August"); break;
            case "October": btnmo.setText("September"); break;
            case "November": btnmo.setText("October"); break;
            case "December": btnmo.setText("November"); break;
            default: btnmo.setText("January"); break;
        }
    }
    
    public static String datePickerLogic(JButton btn, JTextField day, JTextField yr) {
        //use this function right before clearsearch everytime there is changes in database for referesh
        String mm;
        String dd;
        String yyyy;
        StringBuilder deserch = new StringBuilder();
        try{
            //month
            String month = btn.getText();
            switch(month){
                case "January": mm="01"; break;
                case "February": mm="02"; break;
                case "March": mm="03"; break;
                case "April": mm="04"; break;
                case "May": mm="05"; break;
                case "June": mm="06"; break;
                case "July": mm="07"; break;
                case "August": mm="08"; break;
                case "September": mm="09"; break;
                case "October": mm="10"; break;
                case "November": mm="11"; break;
                case "December": mm="12"; break;
                default: mm="01";btn.setText("January");break;
            }
            //day
            int iday = Integer.parseInt(day.getText());
            switch (iday){
                case 1:dd="01";break;
                case 2:dd="02";break;
                case 3:dd="03";break;
                case 4:dd="04";break;
                case 5:dd="05";break;
                case 6:dd="06";break;
                case 7:dd="07";break;
                case 8:dd="08";break;
                case 9:dd="09";break;    
                default: dd="01";break;
            }
            if(iday>=10 && iday<=31){
                dd = String.valueOf(iday);
            }
            //year
            yyyy = yr.getText();
            deserch.append(mm);
            deserch.append(dd);
            deserch.append(yyyy);
        }catch(NumberFormatException e){
            day.setText("1");
            yr.setText("2024");
        }
        
        return String.valueOf(deserch);
        
    }
    public static void btnMoSearch(JButton btn, JTextField day, JTextField yr, JTable table) {
        //use this function right before clearsearch everytime there is changes in database for referesh
        
        String query = datePickerLogic(btn, day, yr);
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
        table.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));
        
    }
    public static void btnReportRefreshTable(){
        populateTable(pnlReport.jTable1, "money");
        //pnlReport.jLabel2.setText(String.valueOf(moneyComma(totalCapital())));
        updateCapital();
        
        String query = "";
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
        pnlReport.jTable1.setRowSorter(tr);
        pnlReport.jTextArea1.setText("");
        tr.setRowFilter(RowFilter.regexFilter(query));
    }
    public static void btnReportClear(){
        pnlReport.jButton1.setText("January");
        pnlReport.jTextField1.setText("1");
        pnlReport.jTextField2.setText("2024");
        pnlReport.jTextField3.setText("");
        pnlReport.jTextArea1.setText("");
        Fns.btnReportRefreshTable();
    }
    public static void btnNewTransact(JFrame frame){
        dlgtransactcash.setLocationRelativeTo(frame);
        dlgtransactcash.setVisible(true);
    }
    public static void btnSaveCashTransact(JButton btn, JTextField day, JTextField yr, JTextField tfamount, JTextArea ta){
        int id = random6int();
        String sid = String.valueOf(id);
        while(existing("money","id",sid)){
            id=random6int();
            sid=String.valueOf(id);
        }
        if(!cannum(day.getText())||!cannum(yr.getText())||!cannum(tfamount.getText())){
            JOptionPane.showMessageDialog(frmdashboard,"Please ensure number input.");
        }
        String date = datePickerLogic(btn, day, yr);
        String longdate = translateDateString(date);
        String amount="";
        if(cannum(tfamount.getText())){
            amount=tfamount.getText();
        }
        String description = "";
        
        String dsc = receiptMaker(ta.getText(), date, amount, sid);
        description = String.valueOf(dsc);
        
        
        
        try{
                       
            String insertSQL = "INSERT INTO money (id, date, amount, desc) VALUES (?, ?, ?, ?)";
            PreparedStatement pps = connection.prepareStatement(insertSQL);
            pps.setString(1, sid);
            pps.setString(2, date);
            pps.setString(3, amount);
            pps.setString(4, description);
            
            int rowsAffected = pps.executeUpdate();
            pps.close();
            
            JOptionPane.showMessageDialog(frmdashboard, "Saved successfully!");
            
            dlgtransactcash.dispose();

            day.setText("1");
            yr.setText("2024");
            tfamount.setText("");
            ta.setText("");
        
        }catch(SQLException e){
            e.printStackTrace();
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(frmdashboard, "Please ensure number input.");
        }
        
    }
    public static void pnlReportTableClick(){
        if(pnlReport.jTable1.getSelectedRow()!=-1){
            try(Statement statement = Fns.connection.createStatement()){
                Object pkey = Fns.getid(pnlReport.jTable1);
                String query = "SELECT * FROM money WHERE id = '"+pkey+"'";
                ResultSet resultSet = statement.executeQuery(query);
                
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                if(resultSet.next()){
                    String srs = "";
                    for(int i = 1; i<= columnCount; i++){
                        srs = String.valueOf(resultSet.getObject(i));
                        if(i==4){
                            pnlReport.jTextArea1.setText(srs);
                        }
                    }
                }
                
            }catch(SQLException e){
                
            }
        }
    }
    public static String receiptMaker(String text, String dpl, String amount, String id){
        StringBuilder sb = new StringBuilder();
        sb.append("*****RECEIPT*****\n\n");
        sb.append(text);
        sb.append("\n\nDate:\n");
        sb.append(translateDateString(dpl));
        sb.append("\nAmount:\nP");
        sb.append(moneyComma(Long.parseLong(amount)));
        sb.append("\nTransaction Reference ID:\n");
        sb.append(id);
        sb.append("\n\n*****************");
        String s = String.valueOf(sb);
        return s;
    }
    public static String receiptIdSaveProd(){
        int id = random6int();
        String sid = String.valueOf(id);
        while(existing("money","id",sid)){
            id=random6int();
            sid=String.valueOf(id);
        }
        return sid;
    }
    public static String receiptDateSaveProd(){
        return datePickerLogic(dlgSaveAddProduct.jButton12, dlgSaveAddProduct.jTextField6, dlgSaveAddProduct.jTextField8);
    } 
    public static String receiptAmountSaveProd(){
        int cost = intforcezero(dlgSaveAddProduct.jTextField3.getText());
        int qty = intforcezero(dlgSaveAddProduct.jTextField2.getText());
        int namount = -1*(cost*qty);
        return String.valueOf(namount);
    }
    public static String receiptDescriptionSaveProd(){
        //id
        String sid = receiptIdSaveProd();
        //date
        //String date = translateDateString(receiptDateSaveProd());
        String date = receiptDateSaveProd();
        //amount
        String amount = receiptAmountSaveProd();
        //desc
        StringBuilder sb = new StringBuilder();
        sb.append(dlgSaveAddProduct.jTextField1.getText());
        sb.append("  -  \n");
        String cat = dlgSaveAddProduct.jButton1.getText();
        if(!cat.equals("CATEGORY")){            
            sb.append(cat);
            sb.append("\nQuantity: ");
        }else{
            sb.append("\nQuantity: ");
        }
        String sqty = dlgSaveAddProduct.jTextField2.getText();
        String scost = dlgSaveAddProduct.jTextField3.getText();
        String ssell = dlgSaveAddProduct.jTextField4.getText();
        String lst = dlgSaveAddProduct.jTextField5.getText();
        sb.append(sqty);
        sb.append("x\nCost per piece:\n");
        sb.append(scost);
        sb.append("\nRetail Price:\n");
        sb.append(ssell);        
        sb.append("\n\nLow Stock Threshold:  x");
        String text = String.valueOf(sb);
        return receiptMaker(text, date, amount, sid);
        
    }
}
