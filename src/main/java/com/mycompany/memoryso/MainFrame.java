/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.memoryso;

import Classes.Planner;
import com.sun.tools.javac.Main;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author danie
 */
public class MainFrame extends javax.swing.JFrame {

    Reloj horaActual = new Reloj(); // hilo que muestra la hora del sistema
    List<Classes.Process> processList = new ArrayList<Classes.Process>();
    Planner planner = null;
    PlannerThread plannerThread = new PlannerThread();
    cpuInterface cpu = new cpuInterface();
    memoryInterface memory = new memoryInterface();
    DefaultTableModel model;
    boolean memoryDrawed = false;
    int maxMemory;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        this.maxMemory = 45;
        horaActual.start();
        cpu.start();
        memory.start();
        model = (DefaultTableModel) jTable1.getModel();
        this.lblMemory.setBorder(BorderFactory.createLineBorder(Color.RED, 5));
        this.lblSO.setBorder(BorderFactory.createLineBorder(Color.RED, 5));
        this.lblSO.setOpaque(true);
        this.lblSO.setBackground(Color.CYAN);

    }

    public class memoryInterface extends Thread {

        @Override
        public void run() {
            while (true) {

                try {
                    if (planner != null && memoryDrawed && planner.getCurrentProcess() != null) {
                        fillMemory();
                    }
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public class cpuInterface extends Thread {

        @Override
        public void run() {

            while (true) {

                if (planner != null && planner.getCurrentProcess() != null) {

                    lblCurrentProcess.setText(planner.getCurrentProcess().getId());
                    lblBase.setText(planner.getCurrentProcess().getBase());
                    lblLimit.setText(planner.getCurrentProcess().getLimit());
                    lbld.setText(String.valueOf(planner.getCurrentProcess().getProgrammCount()));

                }
                if (planner != null && planner.getProcessList().isEmpty()) {
                    if (planner.getCurrentProcess() == null || planner.getCurrentProcess().getIntakeTime() <= 0) {
                        lblCurrentProcess.setText("Terminado");
                    }
                }

                model.setRowCount(0);
                if (planner != null) {

                    Object[] fila1 = new Object[5];
                    if (planner.getCurrentProcess() != null) {
                        fila1[0] = planner.getCurrentProcess().getId();
                        fila1[1] = planner.getCurrentProcess().getStatus();
                        fila1[2] = planner.getCurrentProcess().getIntakeTime();
                        fila1[3] = planner.getCurrentProcess().getStartTime();
                        model.addRow(fila1);
                    }
                    for (Classes.Process process : planner.getProcessList()) {

                        fila1[0] = process.getId();
                        fila1[1] = process.getStatus();
                        fila1[2] = process.getIntakeTime();
                        fila1[3] = process.getStartTime();
                        model.addRow(fila1);
                    }

                    if (planner.getProcessList().isEmpty() && planner.getCurrentProcess() == null) {

                        for (Classes.Process process : planner.getFinishedProcess()) {

                            fila1[0] = process.getId();
                            fila1[1] = process.getStatus();
                            fila1[2] = process.getIntakeTime();
                            fila1[3] = process.getStartTime();
                            fila1[4] = process.getEndTime();
                            model.addRow(fila1);
                        }

                    }

                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public class PlannerThread extends Thread {

        @Override
        public void run() {

            planner.roundRobbin();
        }
    }

    public class Reloj extends Thread {

        Calendar calendario;

        @Override
        public void run() {
            while (true) {
                String horaSistema = "";
                calendario = Calendar.getInstance();
                if (calendario.get(Calendar.HOUR_OF_DAY) < 10) {
                    horaSistema += String.valueOf("0" + calendario.get(Calendar.HOUR_OF_DAY)) + ":";
                } else {
                    horaSistema += String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)) + ":";
                }
                if (calendario.get(Calendar.MINUTE) < 10) {
                    horaSistema += String.valueOf("0" + calendario.get(Calendar.MINUTE)) + ":";
                } else {
                    horaSistema += String.valueOf(calendario.get(Calendar.MINUTE)) + ":";
                }
                if (calendario.get(Calendar.SECOND) < 10) {
                    horaSistema += String.valueOf("0" + calendario.get(Calendar.SECOND)) + ":";
                } else {
                    horaSistema += String.valueOf(calendario.get(Calendar.SECOND)) + ":";
                }
                horaSistema += String.valueOf(calendario.get(Calendar.MILLISECOND)) + " hrs";
                lblHoraSistema.setText(horaSistema);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    public void drawMemory(List<Classes.Process> list) {

        this.lblMemory.removeAll();
        lblMemory.revalidate();
        lblMemory.repaint();
        //Collections.sort(list);
        int posY = 5;
        for (int i = 0; i < list.size(); i++) {
            System.out.println("Draw");
            JLabel temp = new JLabel("Process " + list.get(i).getId());
            temp.setVerticalAlignment(SwingConstants.CENTER);
            temp.setHorizontalAlignment(SwingConstants.CENTER);
            temp.setBounds(5, posY, 190, 10 * list.get(i).getIntakeTime());
            posY = 10 * list.get(i).getIntakeTime() + posY + 1;
            Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
            temp.setBorder(border);
            processList.get(i).setBase(String.valueOf(temp.getBounds().y));
            processList.get(i).setLimit(String.valueOf(temp.getBounds().y + temp.getBounds().height));
            processList.get(i).setProgrammCount(Integer.valueOf(processList.get(i).getBase()));
            temp.setName(list.get(i).getId());

            this.lblMemory.add(temp);
            this.lblMemory.setText("");

        }
        memoryDrawed = true;

    }

    public void fillMemory() {

        for (Component componente : this.lblMemory.getComponents()) {
            // Verifica si el componente es un JLabel
            if (componente instanceof JLabel) {

                if (componente.getName().equals(planner.getCurrentProcess().getId())) {

                    if (((JLabel) componente).getComponentCount() == 0) {

                        JLabel temp2 = new JLabel("Process: " + componente.getName());
                        temp2.setVerticalAlignment(SwingConstants.CENTER);
                        temp2.setHorizontalAlignment(SwingConstants.CENTER);
                        int height = (componente.getSize().height / 10) - planner.getCurrentProcess().getIntakeTime();
                        temp2.setBounds(0, 0, 200, 10 * (height));
                        Border border2 = BorderFactory.createLineBorder(Color.GREEN, 1);
                        temp2.setBorder(border2);
                        temp2.setOpaque(true);
                        temp2.setBackground(Color.GREEN);
                        ((JLabel) componente).add(temp2);
                        ((JLabel) componente).setText("");
                    } else {

                        int height = (componente.getSize().height / 10) - planner.getCurrentProcess().getIntakeTime();

                        height = (height + 1) * 10;

                        Dimension dm = new Dimension(200, height);

                        ((JLabel) componente).getComponent(0).setSize(dm);
                        componente.revalidate();
                        componente.repaint();

                    }

                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCurrentProcess2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblCurrentProcess3 = new javax.swing.JLabel();
        txtIntakeTime = new javax.swing.JTextField();
        lbld = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        lblBase = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        lblLimit = new javax.swing.JLabel();
        lblHoraSistema = new javax.swing.JLabel();
        lblMemory = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblSO = new javax.swing.JLabel();
        lblProgrammCounter = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        lblCurrentProcess = new javax.swing.JLabel();
        txtArrivalTime = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblCurrentProcess2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCurrentProcess2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurrentProcess2.setText("Base");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Intake Time");

        lblCurrentProcess3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCurrentProcess3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurrentProcess3.setText("Limit");

        lbld.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbld.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbld.setText("d");

        jButton2.setText("Cargar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        lblBase.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBase.setText("b");

        jButton3.setText("Limpiar memoria");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("INIT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lblLimit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblLimit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLimit.setText("h");

        lblHoraSistema.setText("Hora sistema");

        lblMemory.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMemory.setText("Memory");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel10.setText("CPU");

        lblSO.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSO.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSO.setText("SISTEMA OPERATIVO");

        lblProgrammCounter.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblProgrammCounter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProgrammCounter.setText("Programm Counter");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("ID:");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Status", "Intake Time", "Start Time", "End Time"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Arrival Time");

        lblCurrentProcess.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblCurrentProcess.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurrentProcess.setText("current Process");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMemory, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSO, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lblHoraSistema)
                                .addGap(62, 62, 62))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(174, 174, 174))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblProgrammCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lbld, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(lblCurrentProcess3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(lblLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(lblCurrentProcess2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(lblBase, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(107, 107, 107))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(229, 229, 229))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(90, 90, 90)
                                        .addComponent(jButton2)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(lblCurrentProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(162, 162, 162))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtArrivalTime, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtIntakeTime, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblMemory, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSO, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(7, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblHoraSistema)
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(txtArrivalTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txtIntakeTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCurrentProcess)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbld)
                            .addComponent(lblProgrammCounter))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCurrentProcess2)
                            .addComponent(lblBase))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCurrentProcess3)
                            .addComponent(lblLimit))
                        .addGap(37, 37, 37)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String name = txtName.getText();
        int arrivalTime = Integer.valueOf(txtArrivalTime.getText());
        int intakeTime = Integer.valueOf(txtIntakeTime.getText());
        if (maxMemory - intakeTime >= 0) {
            processList.add(new Classes.Process(name, arrivalTime, intakeTime));
            drawMemory(processList);
            txtName.setText("");
            txtIntakeTime.setText("");
            txtArrivalTime.setText("");
            maxMemory = maxMemory - intakeTime;
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
          // TODO add your handling code here:
        processList.clear();
        drawMemory(processList);
        this.maxMemory =0;
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        drawMemory(processList);

        planner = new Planner(processList, 5);

        plannerThread = new PlannerThread();
        plannerThread.start();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblBase;
    private javax.swing.JLabel lblCurrentProcess;
    private javax.swing.JLabel lblCurrentProcess2;
    private javax.swing.JLabel lblCurrentProcess3;
    private javax.swing.JLabel lblHoraSistema;
    private javax.swing.JLabel lblLimit;
    private javax.swing.JLabel lblMemory;
    private javax.swing.JLabel lblProgrammCounter;
    private javax.swing.JLabel lblSO;
    private javax.swing.JLabel lbld;
    private javax.swing.JTextField txtArrivalTime;
    private javax.swing.JTextField txtIntakeTime;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
