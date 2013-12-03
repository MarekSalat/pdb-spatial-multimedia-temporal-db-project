package cz.vutbr.fit.pdb.nichcz.gui.dialogs;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.setting.Setting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField userNameTextField;
    private JPasswordField passwordPasswordField;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JLabel messageLabel;
    private JCheckBox rememberCheckBox;

    private Context ctx;

    private final InputVerifier inputVerifier = new InputVerifier() {
        @Override
        public boolean verify(JComponent input) {
            JTextField field = (JTextField) input;
            return !field.getText().isEmpty(); // && field.getText().length() > 3;
        }
    };

    public LoginDialog(Frame frame, Context ctx) {
        super(frame);

        this.ctx = ctx;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        contentPane.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                return inputVerifier.verify(userNameTextField) && inputVerifier.verify(passwordPasswordField);
            }
        });

        userNameTextField.setInputVerifier(inputVerifier);
        passwordPasswordField.setInputVerifier(inputVerifier);

        //Make this dialog display it.
        setContentPane(contentPane);

        setData(ctx.setting.user);

        pack();
    }

    private void onOK() {
        if(!contentPane.getInputVerifier().verify(contentPane)){
            JOptionPane.showMessageDialog(null, "Invalid data");
            return;
        }

        if(isModified(ctx.setting.user)){
            getData(ctx.setting.user);
        }
        ctx.setting.user.logged = true;
        System.out.println(ctx.setting.user);

        dispose();
    }

    public void setData(Setting.User data) {
        rememberCheckBox.setSelected(data.isRememberCredentials());
        userNameTextField.setText(data.getName());
        passwordPasswordField.setText(data.getPassword());
    }

    public void getData(Setting.User data) {
        data.setRememberCredentials(rememberCheckBox.isSelected());
        data.setName(userNameTextField.getText());
        data.setPassword(passwordPasswordField.getText());
    }

    public boolean isModified(Setting.User data) {
        if (rememberCheckBox.isSelected() != data.isRememberCredentials()) return true;
        if (userNameTextField.getText() != null ? !userNameTextField.getText().equals(data.getName()) : data.getName() != null)
            return true;
        if (passwordPasswordField.getText() != null) {
            if (!passwordPasswordField.getText().equals(data.getPassword()))
                return true;
        } else {
            if (data.getPassword() != null)
                return true;
        }
        return false;
    }
}
