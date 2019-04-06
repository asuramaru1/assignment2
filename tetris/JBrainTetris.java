package tetris;

import javax.swing.*;
import java.awt.*;

public class JBrainTetris extends JTetris   {
    private DefaultBrain brain;
    private  JCheckBox brainMode;
    private JCheckBox animate;
    private int checkCount;
    private Brain.Move nextMove;
    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    JBrainTetris(int pixels) {
        super(pixels);
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
        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active");
        panel.add(brainMode);
        panel.add(new JLabel("Animate DROP:"));
        animate = new JCheckBox("Animate" , true);
        panel.add(animate);
        return panel;
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
        frame.setVisible(true);
    }

    }


