/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package books;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Admin
 */
public class BookWindow extends JFrame{
    private JLabel labelTytul = new JLabel("Tytuł:", JLabel.RIGHT);
    private JTextField textfieldTytul = new JTextField("", 20);
    private JLabel labelTom = new JLabel("Tom:", JLabel.RIGHT);
    private JTextField textfieldTom = new JTextField("", 20);
    private JLabel labelRokWydania = new JLabel("Rok wydania:", JLabel.RIGHT);
    private JTextField textfieldRokWydania = new JTextField("", 20);
    private JLabel labelRodzaj = new JLabel("Rodzaj:", JLabel.RIGHT);
    private JTextField textfieldRodzaj = new JTextField("", 20);
    private JLabel labelIsbn = new JLabel("ISBN:", JLabel.RIGHT);
    private JTextField textfieldIsbn = new JTextField("", 20);
    private JLabel labelAutor = new JLabel("Autor:", JLabel.RIGHT);
    private JComboBox comboboxAutor = new JComboBox();
   
    public BookWindow()
    {
        super("Edycja książki");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350,300);
        setVisible(true);
        
        JPanel labelPanel = new JPanel(new GridLayout(6, 1));
        JPanel fieldPanel = new JPanel(new GridLayout(6, 1));
        add(labelPanel, BorderLayout.WEST);
        add(fieldPanel, BorderLayout.CENTER);
        
        labelTytul.setLabelFor(textfieldTytul);
        labelTom.setLabelFor(textfieldTom);
        labelRokWydania.setLabelFor(textfieldRokWydania);
        labelRodzaj.setLabelFor(textfieldRodzaj);
        labelIsbn.setLabelFor(textfieldIsbn);
        labelAutor.setLabelFor(comboboxAutor);
        labelPanel.add(labelTytul);
        labelPanel.add(labelTom);
        labelPanel.add(labelRokWydania);
        labelPanel.add(labelRodzaj);
        labelPanel.add(labelIsbn);
        labelPanel.add(labelAutor);
        
        
        
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(textfieldTytul);
        fieldPanel.add(p);
        
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p2.add(textfieldTom);
        fieldPanel.add(p2);
        
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p3.add(textfieldRokWydania);
        fieldPanel.add(p3);
        
        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p4.add(textfieldRodzaj);
        fieldPanel.add(p4);
        
        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p5.add(textfieldIsbn);
        fieldPanel.add(p5);
        
        JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p6.add(comboboxAutor);
        fieldPanel.add(p6);
        
        JPanel panel = new JPanel();
        panel.add(new ButtonSubmit());
        add(panel, BorderLayout.SOUTH);
        comboboxAutor.setPreferredSize(new Dimension(225,25));
        
        FillAuthorsCombo();
    }
    class ButtonSubmit extends JButton implements ActionListener {

        ButtonSubmit() {
                super("Zatwierdź");
                addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Submit();
        }
    }
    private void FillAuthorsCombo()
    {
        ServerResponse sr = Server.GetAuthors();
        if(sr.Status == 0)
        {
            ResultSet resultSet = (ResultSet)sr.ResponseObject;
            comboboxAutor.removeAllItems();
            try {
                while(resultSet.next())
                {
                    String id = resultSet.getString("id");
                    String imie = resultSet.getString("imie");
                    String nazwisko = resultSet.getString("nazwisko");
                    comboboxAutor.addItem(new ComboItem(imie + " " + nazwisko, id));
                }
            } catch (SQLException ex) {
            }
        }
    }
    private void Submit()
    {
        String tytul = textfieldTytul.getText();
        if(tytul.compareTo("") == 0)
        {
            showMessageDialog(null, "Tytuł nie może być pusty");
            return;
        }
        int tom = 0;
        try{
            tom = Integer.parseInt(textfieldTom.getText());
            if(tom <= 0)
            {
                throw new Exception();
            }
        }catch(Exception e)
        {
            showMessageDialog(null, "Tom musi być liczbą większą od zera");
            return;
        }
        
        Date date = new Date();
        String rodzaj = textfieldRodzaj.getText();
        String isbn = textfieldIsbn.getText();
        Object item = comboboxAutor.getSelectedItem();
        String value = ((ComboItem)item).getValue();
        int id = Integer.parseInt(value);
        try {
            date = new SimpleDateFormat("yyyy").parse(textfieldRokWydania.getText());
        } catch (ParseException ex) {
            showMessageDialog(null, "Niepoprawny rok wydania");
            return;
        }
        ServerResponse sr = Server.InsertBook(tytul, tom, new java.sql.Date(date.getTime()),rodzaj, isbn, id);
        if(sr.Status == 0)
        {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
        else
        {
            showMessageDialog(null, "Wystąpił błąd podczas dodawania książki");
        }
    }
}
