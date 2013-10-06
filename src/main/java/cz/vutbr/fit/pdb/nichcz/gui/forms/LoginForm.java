package cz.vutbr.fit.pdb.nichcz.gui.forms;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: Marek Salát
 * Date: 6.10.13
 * Time: 16:33
 */
public class LoginForm extends BaseForm {
    private JPanel panel1;
    private JButton button1;

    public LoginForm() {
        super();
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);    //To change body of overridden methods use File | Settings | File Templates.

                JOptionPane.showMessageDialog(panel1, "thank you for using this ****");
            }
        });
    }

    public Container getPanel() {
        return panel1;
    }
}
