/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.checker.views;

import grammar.checker.ConnectionManager;
import grammar.checker.models.Word;
import grammar.checker.service.CorpusService;
import grammar.checker.TextProcessing;
import grammar.checker.service.CheckerService;
import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Ru'fatiani
 */
public class HomePanel extends javax.swing.JPanel {

    /**
     * Creates new form HomePanel
     */
    
    private File corpus, input, output;
    private ConnectionManager conn;
    
    public HomePanel() {
        initComponents();
        conn = new ConnectionManager();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgMethod = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tfSourceFileCorpus = new javax.swing.JTextField();
        bImport = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        rbRuleFalse = new javax.swing.JRadioButton();
        rbRuleTrue = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        tfBrowseFileInput = new javax.swing.JTextField();
        bSearchInput = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tfBrowseFileOutput = new javax.swing.JTextField();
        bSearchOutput = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        bCheckGrammar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        bImprove = new javax.swing.JButton();

        bgMethod.add(rbRuleFalse);
        bgMethod.add(rbRuleTrue);

        setMaximumSize(new java.awt.Dimension(462, 317));
        setMinimumSize(new java.awt.Dimension(462, 317));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Improve Corpus");

        jLabel2.setText("Source");

        tfSourceFileCorpus.setText("Select a file...");

        bImport.setText("Import");
        bImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bImportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfSourceFileCorpus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bImport, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfSourceFileCorpus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bImport))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Grammar Checker");

        rbRuleFalse.setSelected(true);
        rbRuleFalse.setText("Statictic Method");
        rbRuleFalse.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbRuleFalseStateChanged(evt);
            }
        });

        rbRuleTrue.setText("Rules Method");

        jLabel4.setText("Browse");

        tfBrowseFileInput.setText("Select a file...");

        bSearchInput.setText("Search");
        bSearchInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSearchInputActionPerformed(evt);
            }
        });

        jLabel5.setText("Results");

        jLabel6.setText("File name");

        tfBrowseFileOutput.setText("Select a file...");

        bSearchOutput.setText("Search");
        bSearchOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSearchOutputActionPerformed(evt);
            }
        });

        jLabel7.setText("Document");

        bCheckGrammar.setText("Check");
        bCheckGrammar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCheckGrammarActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 2, 8)); // NOI18N
        jLabel8.setText("rufatiani_2017");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(rbRuleFalse)
                        .addGap(18, 18, 18)
                        .addComponent(rbRuleTrue))
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bCheckGrammar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfBrowseFileInput, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfBrowseFileOutput)))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bSearchOutput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bSearchInput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbRuleFalse)
                    .addComponent(rbRuleTrue))
                .addGap(10, 10, 10)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tfBrowseFileInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSearchInput))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tfBrowseFileOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSearchOutput))
                .addGap(30, 30, 30)
                .addComponent(bCheckGrammar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8))
        );

        bImprove.setText("Improve");
        bImprove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bImproveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bImprove, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bImprove)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bImportActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser();
       
        int returnVal = fc.showOpenDialog(HomePanel.this);
        
        if(returnVal == JFileChooser.APPROVE_OPTION){
            corpus = fc.getSelectedFile();
            tfSourceFileCorpus.setText(corpus.getPath());
            //JOptionPane.showMessageDialog(null, file.getName(), "TITLE", JOptionPane.WARNING_MESSAGE);
            
        }else{
            JOptionPane.showMessageDialog(null, "Open file failed", "TITLE", JOptionPane.WARNING_MESSAGE);
        }
        
    }//GEN-LAST:event_bImportActionPerformed

    private void bImproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bImproveActionPerformed
        // TODO add your handling code here:
        
        String extCorpus[] = corpus.getPath().split(".");
        
        if(!extCorpus[extCorpus.length-1].equals("txt")){
            JOptionPane.showMessageDialog(null, "Please insert txt file.", "MESSAGE", JOptionPane.WARNING_MESSAGE);
        }else{
            CorpusService c;
            try {
                c = new CorpusService();
            
                try {
                    c.improveCorpus(corpus);
                    JOptionPane.showMessageDialog(null, "Success", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(HomePanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(HomePanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(HomePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            } catch (IOException ex) {
                Logger.getLogger(HomePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /*TextProcessing tp = new TextProcessing();
        try {
            if(tp.readFileCorpus(corpus.getPath()))
                JOptionPane.showMessageDialog(null, "Success", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(HomePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(HomePanel.class.getName()).log(Level.SEVERE, null, ex);
        }*/
       
    }//GEN-LAST:event_bImproveActionPerformed

    private void bCheckGrammarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCheckGrammarActionPerformed
        // TODO add your handling code here:
        
        String extInput[], extOutput[];
        extInput = input.getPath().split(".");
        extOutput = output.getPath().split(".");
        
        if(!extInput[extInput.length-1].equals("txt")){
            JOptionPane.showMessageDialog(null, "Please insert txt file.", "MESSAGE", JOptionPane.WARNING_MESSAGE);
        }else if(!extOutput[extOutput.length-1].equals("txt")){
            JOptionPane.showMessageDialog(null, "Please insert txt file.", "MESSAGE", JOptionPane.WARNING_MESSAGE);
        }else{
            TextProcessing tp = new TextProcessing();
            ArrayList<String> content = new ArrayList<>();
            try {
                content = tp.readFile(input.getPath());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(HomePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            boolean stat = false;
            try {
                CheckerService checker = new CheckerService();
                tp.writeFile(output, checker.grammarCheckerFile(rbRuleTrue.isSelected(), content));
                stat = true;
            } catch (IOException ex) {
                Logger.getLogger(HomePanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(HomePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            if(stat==true)
                JOptionPane.showMessageDialog(null, "Checking Complete", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Checking Failed", "MESSAGE", JOptionPane.ERROR_MESSAGE);
        }
 
        
        /*CheckerService checker = null;
            try {
            checker = new CheckerService();
            
            try {
            if(checker.grammarCheckerFile(rbRuleTrue.isSelected(), input, output))
            JOptionPane.showMessageDialog(null, "Success", "MESSAGE", JOptionPane.WARNING_MESSAGE);
            } catch (IOException ex) {
            Logger.getLogger(HomePanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
            Logger.getLogger(HomePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            } catch (IOException ex) {
            Logger.getLogger(HomePanel.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        
    }//GEN-LAST:event_bCheckGrammarActionPerformed

    private void bSearchInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSearchInputActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser();
       
        int returnVal = fc.showOpenDialog(HomePanel.this);
        
        if(returnVal == JFileChooser.APPROVE_OPTION){
            input = fc.getSelectedFile();
            tfBrowseFileInput.setText(input.getPath());
            //JOptionPane.showMessageDialog(null, file.getName(), "TITLE", JOptionPane.WARNING_MESSAGE);
            
        }else{
            JOptionPane.showMessageDialog(null, "Open file failed", "TITLE", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bSearchInputActionPerformed

    private void bSearchOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSearchOutputActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser();
       
        int returnVal = fc.showOpenDialog(HomePanel.this);
        
        if(returnVal == JFileChooser.APPROVE_OPTION){
            output = fc.getSelectedFile();
            tfBrowseFileOutput.setText(output.getPath());
            //JOptionPane.showMessageDialog(null, file.getName(), "TITLE", JOptionPane.WARNING_MESSAGE);
            
        }else{
            JOptionPane.showMessageDialog(null, "Open file failed", "TITLE", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bSearchOutputActionPerformed

    private void rbRuleFalseStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbRuleFalseStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_rbRuleFalseStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCheckGrammar;
    private javax.swing.JButton bImport;
    private javax.swing.JButton bImprove;
    private javax.swing.JButton bSearchInput;
    private javax.swing.JButton bSearchOutput;
    private javax.swing.ButtonGroup bgMethod;
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
    private javax.swing.JRadioButton rbRuleFalse;
    private javax.swing.JRadioButton rbRuleTrue;
    private javax.swing.JTextField tfBrowseFileInput;
    private javax.swing.JTextField tfBrowseFileOutput;
    private javax.swing.JTextField tfSourceFileCorpus;
    // End of variables declaration//GEN-END:variables
}