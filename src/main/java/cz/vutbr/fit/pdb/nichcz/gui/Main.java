package cz.vutbr.fit.pdb.nichcz.gui;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.gui.dialogs.LoginDialog;
import cz.vutbr.fit.pdb.nichcz.gui.spatial.SpatialTabComponent;
import cz.vutbr.fit.pdb.nichcz.setting.Environment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * User: Marek Salát
 * Date: 6.10.13
 * Time: 17:01
 */
public class Main {
    private static Context ctx = new Context();
    public static void main(String[] args) {
        Environment.context = ctx;

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(ctx.load());
            }
        });
    }

    private static void createAndShowGUI(final Context ctx) {
        initLookAndFeel();

        JFrame frame = new JFrame("PDB");

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Spatial", new SpatialTabComponent(ctx));
        tabbedPane.addTab("Multimedia", new JButton());

        frame.setContentPane(tabbedPane);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                ctx.close();
                System.exit(0);
            }
        });

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension d = new Dimension(
            ctx.setting.window.maxX * ctx.setting.window.zoom,
            ctx.setting.window.maxY * ctx.setting.window.zoom
        );
        frame.setPreferredSize(d);
        frame.setSize(d);
        frame.setMinimumSize(d);
        frame.setVisible(true);
        frame.pack();

        System.out.println(ctx.setting.user);

        if(ctx.isUserLogged()) return;

        LoginDialog loginDialog = new LoginDialog(frame, ctx);
        loginDialog.setVisible(true);
    }

    private static void initLookAndFeel() {
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        }
        catch (ClassNotFoundException e) {
            System.err.println("Couldn't find class for specified look and feel:"
                    + lookAndFeel);
            System.err.println("Did you include the L&F library in the class path?");
            System.err.println("Using the default look and feel.");
        }
        catch (UnsupportedLookAndFeelException e) {
            System.err.println("Can't use the specified look and feel ("
                    + lookAndFeel
                    + ") on this platform.");
            System.err.println("Using the default look and feel.");
        }
        catch (Exception e) {
            System.err.println("Couldn't get specified look and feel ("
                    + lookAndFeel
                    + "), for some reason.");
            System.err.println("Using the default look and feel.");
            e.printStackTrace();
        }
    }
}
