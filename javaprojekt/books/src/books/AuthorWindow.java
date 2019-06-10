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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Admin
 */
public class AuthorWindow extends JFrame{
    private JLabel labelImie = new JLabel("Imię:", JLabel.RIGHT);
    private JTextField textfieldImie = new JTextField("", 20);
    private JLabel labelNazwisko = new JLabel("Nazwisko:", JLabel.RIGHT);
    private JTextField textfieldNazwisko = new JTextField("", 20);
    private JLabel labelKrajPochodzenia = new JLabel("Kraj pochodzenia:", JLabel.RIGHT);
    private JTextField textfieldKrajPochodzenia = new JTextField("", 20);
    
    public AuthorWindow()
    {
        super("Edycja autora");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350,150);
        setVisible(true);
        setResizable(false);
        JPanel labelPanel = new JPanel(new GridLayout(3, 1));
        JPanel fieldPanel = new JPanel(new GridLayout(3, 1));
        add(labelPanel, BorderLayout.WEST);
        add(fieldPanel, BorderLayout.CENTER);
        
        labelImie.setLabelFor(textfieldImie);
        labelNazwisko.setLabelFor(textfieldNazwisko);
        labelKrajPochodzenia.setLabelFor(textfieldKrajPochodzenia);
        labelPanel.add(labelImie);
        labelPanel.add(labelNazwisko);
        labelPanel.add(labelKrajPochodzenia);
        
        
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(textfieldImie);
        fieldPanel.add(p);
        
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p2.add(textfieldNazwisko);
        fieldPanel.add(p2);
        
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p3.add(textfieldKrajPochodzenia);
        fieldPanel.add(p3);
        
        JPanel panel = new JPanel();
        panel.add(new ButtonSubmit());
        add(panel, BorderLayout.SOUTH);
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
    private void Submit()
    {
        String imie = textfieldImie.getText();
        if(imie.compareTo("") == 0)
        {
            showMessageDialog(null, "Imię nie może być puste");
            return;
        }
        String nazwisko = textfieldNazwisko.getText();
        if(nazwisko.compareTo("") == 0)
        {
            showMessageDialog(null, "Nazwisko nie może być puste");
            return;
        }
        String krajPochodzenia = textfieldKrajPochodzenia.getText();
        ServerResponse sr = Server.InsertAutor(imie, nazwisko, krajPochodzenia);
        if(sr.Status == 0)
        {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
        else
        {
            showMessageDialog(null, "Wystąpił błąd podczas dodawania autora");
        }
    }
}
