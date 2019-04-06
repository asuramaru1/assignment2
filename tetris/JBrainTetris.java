package tetris;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class JBrainTetris extends JTetris   {
    private DefaultBrain brain;
    private  JCheckBox brainMode;
    private JCheckBox animate;
    private int checkCount;
    private Random random;
    private JLabel status;
    private Brain.Move nextMove;
    private JSlider adversary;
    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    JBrainTetris(int pixels) {
        super(pixels);
        random = new Random();
        checkCount = 0 ;
        nextMove = null;
        brain = new DefaultBrain();

    }
    @Override
    public  void startGame(){
        super.startGame();
        checkCount = 0 ;
        nextMove = null;
    }
    @Override
    public JComponent createControlPanel() {
        JPanel panel = (JPanel) super.createControlPanel();
        addBrain(panel);
        addAnimate(panel);
        addAdversary(panel);

        return panel;
    }

    private void addAdversary(JPanel panel) {
        // make a little panel, put a JSlider in it. JSlider responds to getValue()
        JPanel little = new JPanel();
        little.add(new JLabel("Adversary:"));
        adversary = new JSlider(0, 100, 0); // min, max, current
        adversary.setPreferredSize(new Dimension(100,15));
        little.add(adversary);
        status = new JLabel(" OK ");
        little.add(status);
        panel.add(little);
// now add little to panel of controls
    }

    private void addAnimate(JPanel panel) {
        panel.add(new JLabel("Animate DROP:"));
        animate = new JCheckBox("Animate" , true);
        panel.add(animate);
    }

    private void addBrain(JPanel panel) {
        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active");
        panel.add(brainMode);
    }

    @Override
    public void tick(int verb){
        if(brainMode.isSelected() && verb==DOWN && inGameWindow()){
            if(checkCount<super.count) {
                checkCount++;
                Movenext();
            }
            if(!animate.isSelected() && super.currentX == nextMove.x
                    && super.currentPiece.equals(nextMove.piece) && super.currentY > nextMove.y) super.tick(DROP) ;
             if(!super.currentPiece.equals(nextMove.piece))
                 super.tick(ROTATE);
            if (currentX < nextMove.x)
                super.tick(RIGHT);
             if(super.currentX> nextMove.x)
                 super.tick(LEFT);
        }
            super.tick(verb);
    }
    @Override
    public Piece pickNextPiece() {
        if(random.nextInt(99)<adversary.getValue() && adversary.getValue()!=0){
            double best = -1;
            Piece result = null ;
            for(int i = 0 ; i<pieces.length ; i++){
                Piece P = pieces[i];
                nextMove = brain.bestMove(super.board ,P  ,super.HEIGHT+super.TOP_SPACE ,  nextMove);
                if(nextMove.score>best) {
                    best = nextMove.score;
                    result = nextMove.piece;
                }
            }
            status.setText("\"OK\"");
            return result;
        }else{
            status.setText(" OK ");
            return super.pickNextPiece();
        }

    }

    private boolean inGameWindow(){
        return super.HEIGHT> super.currentY;
    }
    private void Movenext(){
        super.board.undo();
        nextMove = brain.bestMove(super.board ,super.currentPiece  ,super.HEIGHT+super.TOP_SPACE ,  nextMove);
    }
    public static void main(String[] args) {
        try {
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ignored) { }

        JBrainTetris tetris = new JBrainTetris(16);
        JFrame frame = JBrainTetris.createFrame(tetris);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        int x = screenSize.width / 2;
        int y = screenSize.height / 2;
        frame.setLocation(x-x/3, y-2*y/3);
        frame.setSize(400,500);
        frame.setVisible(true);
    }

    }


