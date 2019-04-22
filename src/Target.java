import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class Target extends TimerTask {
    private double x = 0;
    private double y = 0;
    private Random random = new Random();

    @Override
    public void run(){
        calculateXY();
    }
    private void calculateXY(){
        double angle = Math.toRadians((random.nextDouble()*360)%360);
        x = Math.cos(angle);
        y = Math.sin(angle);
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
}
