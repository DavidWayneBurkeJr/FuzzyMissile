import java.applet.Applet;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class FuzzyMissile extends Applet {

    private static double x = 0;
    private static double y = 0;
    private int numEnemies = 10;
    private Target [] enemies;
    private static double[][] enemyCoords;
    private int missileX = 245;
    private int missileY = 500;
    private ArrayList<Point> missilePath = new ArrayList<>();
    private Image dbImage;
    private Graphics dbg;
    private Random random;
    private Target target = new Target();

    public void init() {
        this.setSize(500, 500);
        random = new Random();
        Timer targetTimer = new Timer();
        targetTimer.schedule(target, 0, 1000);
        enemyCoords = new double[numEnemies][2];
        enemies = new Target[numEnemies];
        Timer enemyTimer = new Timer();
        for(int i = 0; i < numEnemies; i++){
            enemies[i] = new Target();
            enemyTimer.schedule(enemies[i], 0, 1000);
            enemyCoords[i][0] = random.nextDouble()%500;
            enemyCoords[i][1] = random.nextDouble()%500;
        }
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                updateXY();
                double deltaX = findMissileDelta(x, missileX);
                double deltaY = findMissileDelta(y, missileY);
                for(int i = 0; i < numEnemies; i++){
                    double[] values = isClose(enemyCoords[i][0], enemyCoords[i][1], missileX, missileY);
                    double enemyDeltaX = values[0];
                    double enemyDeltaY = values[1];
                    missileX -= enemyDeltaX;
                    missileY -= enemyDeltaY;
                }
                if ((aboutZero(deltaX) == 1 && aboutZero(deltaY) == 1)) {
                    t.cancel();
                }
                missileX += deltaX;
                missileY += deltaY;
                Point currentPoint = new Point(missileX, missileY);
                missilePath.add(currentPoint);
                repaint();

            }
        }, 0, 10);
    }

    public void paint(Graphics g) {
        g.setColor(Color.green);
        g.fillRect((int) x, (int) y, 10, 10);
        for(int i = 0; i < numEnemies; i++){
            g.setColor(Color.pink);
            g.fillOval((int) enemyCoords[i][0] - 20, (int) enemyCoords[i][1] -20, 40, 40);
            g.setColor(Color.red);
            g.fillRect((int) enemyCoords[i][0] -5, (int) enemyCoords[i][1], 10, 10);
        }
        g.setColor(Color.red);
        g.fillOval(missileX-4, missileY-4, 8, 8);
        g.setColor(Color.black);
        for (Point point : missilePath) {
            g.fillRect(point.x, point.y, 1, 1);
        }

    }

    @Override
    public void update(Graphics g) {
        dbImage = createImage(this.getSize().width, this.getSize().height);
        dbg = dbImage.getGraphics();
        if (dbImage == null) {
        }
        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);
        dbg.setColor(getForeground());
        paint(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }

    private double[] isClose(double eX, double eY, int mX, int mY){
        double distance = Math.sqrt(Math.pow((mX - eX), 2) + Math.pow((mY - eY), 2));
        double y = mY - eY;
        double x = mX - eX;
        double[] values = new double[2];
         if (distance < 40 && distance > -40){
             double angle = Math.asin(y/distance);
             values[0] = Math.sin(angle);
             values[1] = Math.cos(angle);
             return values;
         }
         values[0] = 0;
         values[1] = 0;
         return values;
    }

    private double positive(double x) {
        if (x >= 10) {
            return 1;
        }
        if (x > 0) {
            return x / 10.0;
        }
        return 0;
    }

    private double negative(double x) {
        if (x <= -10) {
            return 1;
        }
        if (x < 0) {
            return (-1) * x / 10.0;
        }
        return 0;
    }

    private double aboutZero(double x) {
        if (x >= 2 || x <= -2) {
            return 0;
        }
        if (x == 0) {
            return 1;
        }
        return 0.5;
    }

    private int findMissileDelta(double xOry, int missileXorY) {
        double distance = xOry - missileXorY;
        double m1 = negative(distance);
        double m2 = positive(distance);
        double m3 = aboutZero(distance);
        return (int) (m1 * -1 + m2 * 1);

    }

    private void updateXY() {
        x = ((x + 3*target.getX()) % 500 + 500) % 500;
        y = ((y + 3*target.getY()) % 500 + 500) % 500;
        for(int i = 0; i < numEnemies; i++){
            enemyCoords[i][0] = ((enemyCoords[i][0] + enemies[i].getX()) % 500 + 500) % 500;
            enemyCoords[i][1] = ((enemyCoords[i][1] + enemies[i].getY()) % 500 + 500) % 500;

        }
    }
}
