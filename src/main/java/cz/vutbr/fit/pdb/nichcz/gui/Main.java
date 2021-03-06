package cz.vutbr.fit.pdb.nichcz.gui;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.gui.dialogs.LoginDialog;
import cz.vutbr.fit.pdb.nichcz.gui.media.MultimediaTabComponent;
import cz.vutbr.fit.pdb.nichcz.gui.spatial.SpatialTabComponent;
import cz.vutbr.fit.pdb.nichcz.gui.temporal.TemporalTabComponent;
import cz.vutbr.fit.pdb.nichcz.model.InitDBMapper;
import cz.vutbr.fit.pdb.nichcz.setting.Environment;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * User: Marek Salát
 * Date: 6.10.13
 * Time: 17:01
 *
 * Hlavni trida pro spusteni aplikace a zobrazeni GUI.
 */
public class Main {
    private static Context ctx = new Context();
    private static boolean initDB = false;

    private static JTabbedPane tabbedPane;
    private static TemporalTabComponent temporalTabComponent;
    
    public static void main(String[] args) {
        Environment.context = ctx;

        if (args.length == 1) {
            if (args[0].equals("-i") || args[0].equals("--init")) {
                initDB = true;
            }
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(ctx.load());
            }
        });
    }

    private static void createAndShowGUI(final Context ctx) {
        initLookAndFeel();

        JFrame frame = new JFrame("PDB");

        if(!ctx.isUserLogged()) {
            LoginDialog loginDialog = new LoginDialog(frame, ctx);
            loginDialog.setVisible(true);
        }

        if (initDB) {
            InitDBMapper initDBMapper = new InitDBMapper(ctx);
//            System.out.println(initDBMapper.initDB());

            if (!initDBMapper.initDB()) {
                System.err.println("Could not initialize database.");
                ctx.close();
                System.exit(0);
            }

            initDBMapper.loadMedia();
        }

        tabbedPane = new JTabbedPane();
        temporalTabComponent = new TemporalTabComponent(ctx);
        tabbedPane.addTab("Spatial", new SpatialTabComponent(ctx).getContentPane());
        tabbedPane.addTab("Temporal", temporalTabComponent.getContentPane());
        tabbedPane.addTab("Multimedia", new MultimediaTabComponent(ctx).getContentPane());

        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 1) {
                    temporalTabComponent.updateTables();
                }
            }
        });

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
