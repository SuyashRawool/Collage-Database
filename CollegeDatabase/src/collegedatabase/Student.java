
package collegedatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Student extends javax.swing.JFrame {

    String id;
    LoadDB ld;
    public Student(String id) {
        this.id = id;
        this.ld = new LoadDB();

        
        initComponents();
        this.home();
        this.loadAttendance();
        this.loadScore();
        this.loadAccount();
    }

    private void home(){
        ResultSet data = ld.getData("select * from student where sid ="+this.id);
        String emal=new String();
        String usn=new String();
        String lname = new String();
        int deptId=0;
        String fname = new String();
        String add = new String();
        String dept = new String();
        
        try {
            while(data.next()){
                
                fname = data.getString("first_name");
                usn = data.getString("sid");
                lname = data.getString("last_name");
                emal = data.getString("email");
                add = data.getString("address");
                deptId = data.getInt("dept_id");
                
            }
            
            ResultSet deptData = ld.getData("select dept_name from dept where dept_id="+deptId);
            while(deptData.next())
                dept = deptData.getString("dept_name");
        } catch (SQLException ex) {
            Logger.getLogger(faculty.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        jLabel2.setText(String.format(   "USN             : %s", usn));
        first_name.setText(String.format("First Name   : %s", fname));
        last_name.setText(String.format( "Last Name    : %s", lname));
        email.setText(String.format(     "Email            : %s", emal));
        addr.setText(String.format(      "Address        : %s", add));
        Dept.setText(String.format(      "Department : %s", dept));
    }
    
    private void setAttendance(String data){
        jTextArea1.setText(data);
    }
    
    private void loadAttendance(){
        String courseId = new String();
        String date = new String();
        String status = new String();
        String reason = new String();
        
        String fString = new String();
        
        try {
            fString += String.format("%11s|%22s |%11s |%30s\n","Course ID","Date","Status","Reason");
            ResultSet data = ld.getData("select * from attendance2 where sid="+this.id);
            while(data.next()){
                System.out.println("..");
                courseId = data.getString("course_id");
                date = data.getString("tdate");
                status = data.getString("status");
                reason = data.getString("reason");
                
                if(reason == null)
                    reason = "Reason not given";
                
                fString += String.format("%11s |%16s |%15s |%30s\n", courseId,date,status,reason);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(faculty.class.getName()).log(Level.SEVERE, null, ex);
        }
        setAttendance(fString);
    }
    
    private void filterAttendance(){
        String courseCode = course_code.getText();
        String courseId = new String();
        String date = new String();
        String status = new String();
        String reason = new String();
        
        String fString = new String();
        
        if(courseCode.isEmpty()){
            jLabel5.setText("Please Enter Code");
        }else{
            
            ResultSet data = ld.getData("select * from attendance2 where course_id =\'"+courseCode+"\'");
            try {
                
                if(data.next()){
                    fString += String.format("%11s|%20s |%10s |%30s\n","Course ID","Date","Status","Reason");
                    
                    do{
                        courseId = data.getString("course_id");
                        date = data.getString("tdate");
                        status = data.getString("status");
                        reason = data.getString("reason");
                        fString += String.format("%11s |%15s |%12s |%30s\n", courseId,date,status,reason);
                    }while(data.next());
                    
                    setAttendance(fString);
                }else{
                    jLabel5.setText("Wrong Code");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    private void setScore(String data){
        jTextArea2.setText(data);
    }
    
    private void loadScore(){
        String courseCode,courseName,type;
        String fString = new String(String.format("%-15s| %-10s| %-16s| %-10s|%s\n", "Course Code","IA Score", "Type", "Test No.","Course Name"));
        int score,testNo;
        ResultSet data = ld.getData("select score.course_id, course.course_name, course.type, score.ia_score, score.test_no\n" +
                                    "from course\n" +
                                    "inner join score\n" +
                                    "on score.course_id=course.course_id\n" +
                                    "where score.sid="+this.id);
        
        try {
            while(data.next()){
                courseCode = data.getString("course_id");
                courseName = data.getString("course_name");
                type = data.getString("type");
                score = data.getInt("ia_score");
                testNo = data.getInt("test_no");
                fString+= String.format("%-20s| %-14d| %-15s| %-16d|%s\n", courseCode,score , type,testNo,courseName);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
        setScore(fString);
    }
    
    private void filterScore(){
        String courseCode = course_code1.getText();
        String courseName,type;
        int score,testNo;
        
        String fString = new String();
        
        if(courseCode.isEmpty()){
            jLabel7.setText("Please Enter Code");
        }else{
            
            ResultSet data = ld.getData("select score.course_id, course.course_name, course.type, score.ia_score,score.test_no\n" +
                                        "from course\n" +
                                        "inner join score\n" +
                                        "on score.course_id=course.course_id\n" +
                                        "where score.sid="+this.id+" and score.course_id=\'"+courseCode+"\'");
            try {
                
                if(data.next()){
                    fString += String.format("%-16s|%-18s|%-10s|%-10s|%s\n", "Course Code","Course Name", "Type", "Test No.","IA Score");
                    
                    do{
                        courseCode = data.getString("course_id");
                        courseName = data.getString("course_name");
                        type = data.getString("type");
                        score = data.getInt("ia_score");
                        testNo = data.getInt("test_no");
                        fString+= String.format("%-20s %-25s %-15s %-10d %d\n", courseCode, courseName, type,testNo,score);
                    }while(data.next());
                    
                    setScore(fString);
                }else{
                    jLabel5.setText("Wrong Code");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    
    private void loadAccount(){
        String sql = "select addmission_type, fees, sem\n" +
                     "from account\n" +
                     "where sid ="+this.id;
        int fees=0,sem=0;
        String addtype = new String();
        
        ResultSet data = ld.getData(sql);
        
        try {
            while(data.next()){
                fees = data.getInt("fees");
                sem = data.getInt("sem");
                addtype = data.getString("addmission_type");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(fees != 0)
            FEES.setText("Fees to be paid    : "+fees);
        else
            FEES.setText("Fees to be paid    : Fees Paid.");
        
        SEM.setText("Semester              : "+sem);
        ADDTYPE.setText("Addmission type : "+addtype);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        first_name = new javax.swing.JLabel();
        last_name = new javax.swing.JLabel();
        email = new javax.swing.JLabel();
        addr = new javax.swing.JLabel();
        Dept = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        FEES = new javax.swing.JLabel();
        ADDTYPE = new javax.swing.JLabel();
        SEM = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        course_code = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        course_code1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBackground(new java.awt.Color(0, 254, 239));
        jTabbedPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTabbedPane1.setForeground(new java.awt.Color(236, 15, 15));
        jTabbedPane1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(22, 31, 89));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(0, 153, 255)));

        jLabel1.setFont(new java.awt.Font("Tempus Sans ITC", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Welcome");
        jLabel1.setToolTipText("");

        jLabel2.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("jLabel2");

        first_name.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        first_name.setForeground(new java.awt.Color(255, 255, 255));
        first_name.setText("jLabel2");

        last_name.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        last_name.setForeground(new java.awt.Color(255, 255, 255));
        last_name.setText("jLabel2");

        email.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        email.setForeground(new java.awt.Color(255, 255, 255));
        email.setText("jLabel2");

        addr.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        addr.setForeground(new java.awt.Color(255, 255, 255));
        addr.setText("jLabel2");

        Dept.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        Dept.setForeground(new java.awt.Color(255, 255, 255));
        Dept.setText("jLabel2");

        jButton3.setBackground(new java.awt.Color(163, 228, 215));
        jButton3.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(173, 20, 87));
        jButton3.setText("Change Password!");
        jButton3.setToolTipText("");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(first_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(last_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(email, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Dept, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(70, 70, 70))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(first_name)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(last_name)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(email)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addr)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Dept)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(22, 31, 89));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        FEES.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        FEES.setForeground(new java.awt.Color(255, 255, 255));
        FEES.setText("jLabel2");

        ADDTYPE.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        ADDTYPE.setForeground(new java.awt.Color(255, 255, 255));
        ADDTYPE.setText("jLabel2");

        SEM.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        SEM.setForeground(new java.awt.Color(255, 255, 255));
        SEM.setText("jLabel2");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(FEES, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ADDTYPE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(SEM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(FEES)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ADDTYPE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SEM)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Home", jPanel1);

        jPanel4.setBackground(new java.awt.Color(192, 230, 232));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        course_code.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        course_code.setToolTipText("");

        jLabel3.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabel3.setText("Course Code :");
        jLabel3.setToolTipText("");

        jButton1.setBackground(new java.awt.Color(22, 31, 89));
        jButton1.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Filter");
        jButton1.setToolTipText("");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tempus Sans ITC", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 51));
        jLabel5.setText("...");
        jLabel5.setToolTipText("");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(course_code)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jButton1))
                        .addGap(0, 62, Short.MAX_VALUE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jLabel3)
                .addGap(10, 10, 10)
                .addComponent(course_code, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jButton1)
                .addGap(20, 20, 20)
                .addComponent(jLabel5)
                .addContainerGap(101, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(22, 31, 89));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Here's your attendance...");
        jLabel4.setToolTipText("");

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(22, 31, 89));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("My Attendance", jPanel6);

        jPanel8.setBackground(new java.awt.Color(192, 230, 232));
        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabel6.setText("Course Code :");
        jLabel6.setToolTipText("");

        course_code1.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        course_code1.setToolTipText("");

        jButton2.setBackground(new java.awt.Color(22, 31, 89));
        jButton2.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Filter");
        jButton2.setToolTipText("");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tempus Sans ITC", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 0, 51));
        jLabel7.setText("...");
        jLabel7.setToolTipText("");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(course_code1)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jButton2))
                        .addGap(0, 64, Short.MAX_VALUE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jLabel6)
                .addGap(10, 10, 10)
                .addComponent(course_code1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jButton2)
                .addGap(20, 20, 20)
                .addComponent(jLabel7)
                .addContainerGap(101, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(22, 31, 89));
        jPanel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Here's your score...");
        jLabel8.setToolTipText("");

        jTextArea2.setEditable(false);
        jTextArea2.setBackground(new java.awt.Color(22, 31, 89));
        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jTextArea2.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Score", jPanel7);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.filterAttendance();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.filterScore();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new ChangePassword(this.id,"student").setVisible(true);
        
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Student.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Student.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Student.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Student.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Student("1001").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ADDTYPE;
    private javax.swing.JLabel Dept;
    private javax.swing.JLabel FEES;
    private javax.swing.JLabel SEM;
    private javax.swing.JLabel addr;
    private javax.swing.JTextField course_code;
    private javax.swing.JTextField course_code1;
    private javax.swing.JLabel email;
    private javax.swing.JLabel first_name;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JLabel last_name;
    // End of variables declaration//GEN-END:variables
}
