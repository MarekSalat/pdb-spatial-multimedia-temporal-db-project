package cz.vutbr.fit.pdb.nichcz.setting;

import java.awt.*;
import java.io.Serializable;

/**
 * User: Marek Salát
 * Date: 6.10.13
 * Time: 20:52
 */
public class Setting implements Serializable {
    public class User {
        public String name = "";
        public String password = "";

        public boolean logged = false;

        public boolean rememberCredentials = false;

        public User(){
            String name = System.getProperty("name");
            String pass = System.getProperty("password");

            if( (name != null && !name.isEmpty()) && (pass != null && !pass.isEmpty())){
                this.name = name;
                this.password = pass;
                logged = true;
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", password='" + "password" + '\'' +
                    ", logged=" + logged +
                    ", rememberCredentials=" + rememberCredentials +
                    '}';
        }

        public boolean isRememberCredentials() {
            return rememberCredentials;
        }

        public void setRememberCredentials(final boolean rememberCredentials) {
            this.rememberCredentials = rememberCredentials;
        }
    }
    public User user = new User();

    public String connectionString = "";

    public class Window {
        public short maxX = 640;
        public short maxY = 480;
        public short zoom = 1;
    }
    public Window window = new Window();

    public enum Colors {
        emerald(0x2ecc71),
        nephritis( 0x27ae60),
        sun_flower( 0xf1c40f),
        amethyst( 0x9b59b6),
        wisteria( 0x8e44ad),
        pomegranate( 0xc0392b),
        pumpkin( 0xd35400),
        alizarin( 0xe74c3c),
        orange( 0xf39c12),
        carrot( 0xe67e22),
        concrete( 0x95a5a6),
        asbestos( 0x7f8c8d),
        clouds( 0xecf0f1),
        silver( 0xbdc3c7),
        turquoise( 0x1abc9c),
        green_sea( 0x16a085),
        peter_river( 0x3498db),
        belize_hole( 0x2980b9),
        wet_asphalt( 0x34495e),
        midnight_blue( 0x2c3e50)
        ;
        private Color color;
        Colors(int hex){
            color = new Color(hex);
        }

        public int getRGB(){
            return color.getRGB();
        }

        public Color getColor(int aplha){
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), aplha);
        }

        public Color getColor() {
            return getColor(255);
        }
    }
}
