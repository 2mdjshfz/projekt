/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package books;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Admin
 */
public class Client extends JFrame {
    private JTable tableBooks;
    private JTable tableAuthors;
    private JPanel booksTableLayout = new JPanel();
    private JPanel authorsTableLayout = new JPanel();
    public Client()
    {
        super("Siema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,400);
        setVisible(true);
        
        
        Init();
    }
    
    private void CreateBooksTable()
    {
        booksTableLayout.removeAll();
        ServerResponse sr = Server.GetBooks();
        if(sr.Status == 0)
        {
            ResultSet resultSet = (ResultSet)sr.ResponseObject;
            try {
                tableBooks = new JTable(buildTableModel(resultSet));
                JTableHeader header = tableBooks.getTableHeader();
                booksTableLayout.setLayout(new BorderLayout());
                booksTableLayout.add(header, BorderLayout.NORTH);
                booksTableLayout.add(tableBooks, BorderLayout.CENTER);
            } catch (SQLException ex) {
            }
        }
        booksTableLayout.updateUI();
    }
    
    private void CreateAuthorsTable()
    {
        authorsTableLayout.removeAll();
        ServerResponse sr = Server.GetAuthors();
        if(sr.Status == 0)
        {
            ResultSet resultSet = (ResultSet)sr.ResponseObject;
            try {
                tableAuthors = new JTable(buildTableModel(resultSet));
                JTableHeader header = tableAuthors.getTableHeader();
                authorsTableLayout.setLayout(new BorderLayout());
                authorsTableLayout.add(header, BorderLayout.NORTH);
                authorsTableLayout.add(tableAuthors, BorderLayout.CENTER);
            } catch (SQLException ex) {
            }
        }
        authorsTableLayout.updateUI();
    }
    
    private void Init()
    {     
        JPanel mainLayout = new JPanel();
        JPanel topButtonPanel = new JPanel();
        topButtonPanel.setLayout(new FlowLayout());
        topButtonPanel.add(new ButtonDeleteBook());
        topButtonPanel.add(new ButtonAddBook());
        topButtonPanel.add(new ButtonAddAuthor());
        topButtonPanel.add(new ButtonDeleteAuthor());
        BoxLayout boxLayout = new BoxLayout(mainLayout, BoxLayout.PAGE_AXIS);
        mainLayout.add(topButtonPanel);
        mainLayout.setLayout(boxLayout);
        mainLayout.add(booksTableLayout);
        mainLayout.add(authorsTableLayout);
        add(mainLayout);
        
        CreateBooksTable();
        CreateAuthorsTable();
    }
    
    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }
    class ButtonDeleteBook extends JButton implements ActionListener {

        ButtonDeleteBook() {
                super("Usuń książkę");
                addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = tableBooks.getSelectedRow();
            if(selectedRow == -1)
            {
                showMessageDialog(null, "Nie wybrano książki");
            }
            else
            {
                int bookId = (int)tableBooks.getValueAt(selectedRow, 0);
                ServerResponse sr = Server.DeleteBook(bookId);
                if(sr.Status == 0)
                {
                    CreateBooksTable();
                }
            }

        }
    }
    class ButtonAddBook extends JButton implements ActionListener {

        ButtonAddBook() {
                super("Dodaj książkę");
                addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(tableAuthors.getRowCount() == 0)
            {
                showMessageDialog(null, "Najpierw dodaj autorów");
                return;
            }
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JFrame f = new BookWindow();
                    f.addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent e) {
                            f.dispose();
                            CreateBooksTable();
                        }
                    });
                }
            });
        }
    }
    class ButtonAddAuthor extends JButton implements ActionListener {

        ButtonAddAuthor() {
                super("Dodaj autora");
                addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JFrame f = new AuthorWindow();
                    f.addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent e) {
                            f.dispose();
                            CreateAuthorsTable();
                        }
                    });
                }
            });
        }
    }
    class ButtonDeleteAuthor extends JButton implements ActionListener {

        ButtonDeleteAuthor() {
                super("Usuń autora");
                addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = tableAuthors.getSelectedRow();
            if(selectedRow == -1)
            {
                showMessageDialog(null, "Nie wybrano autora");
            }
            else
            {
                int authorId = (int)tableAuthors.getValueAt(selectedRow, 0);
                ServerResponse sr = Server.DeleteAutor(authorId);
                if(sr.Status == 0)
                {
                    CreateAuthorsTable();
                }
                else if (sr.Status == 1)
                {
                    showMessageDialog(null, "Nie można usunąć autora, ponieważ ma przypisaną książkę");
                }
            }
        }
    }
}
