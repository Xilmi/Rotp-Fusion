/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.ui;

import static rotp.model.game.IDebugOptions.AUTORUN_OTHERFILE;
import static rotp.ui.vipconsole.IVIPConsole.SPACER;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints; // modnar: needed for adding RenderingHints
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import rotp.model.empires.Empire;
import rotp.model.empires.GalacticCouncil;
import rotp.model.game.IDebugOptions;
import rotp.ui.main.MainUI;
import rotp.ui.main.SystemPanel;
import rotp.ui.vipconsole.IVIPListener;

public final class GalacticCouncilUI extends FadeInPanel
		implements MouseListener, MouseMotionListener, MouseWheelListener, IVIPListener {
    private static final long serialVersionUID = 1L;
    private enum Display { ANNOUNCE, SHOW_VOTE_RESULT, ASK_PLAYER_VOTE, NO_WINNER, ACCEPT_RULING }
    private Display displayMode;

    public static BufferedImage iconBackImg, raceBackImg, wideBackImg;
    static final Color yellowText = new Color(216,154,0);
    static final Color raceEdgeColor = new Color(44,48,47);
    static final Color raceCenterColor = new Color(110,118,117);
    static final Color backDarkC = new Color(34,53,102);
    static final Color centerC = new Color(79,102,156);
    static final Color maskC  = new Color(40,40,40,160);
    static final Color redEdgeC = new Color(72,14,14);
    static final Color redMidC = new Color(126,28,28);
    static final Color greenEdgeC = new Color(44,59,30);
    static final Color greenMidC = new Color(70,93,48);
    static final Color grayEdgeC = new Color(59,59,59);
    static final Color grayMidC = new Color(93,93,93);
    static final Color scrollBarC = new Color(177,177,177);
    
    private final Rectangle summaryBox = new Rectangle();
    private final Rectangle continueBox = new Rectangle();
    private final Rectangle skipBox = new Rectangle();
    private final Rectangle candidate1Box = new Rectangle();
    private final Rectangle candidate2Box = new Rectangle();
    private final Rectangle abstainBox = new Rectangle();
    private final Rectangle acceptBox = new Rectangle();
    private final Rectangle rejectBox = new Rectangle();
    private final Rectangle scrollbar = new Rectangle();
    private final Rectangle voterListBox = new Rectangle();
    private Shape hoverTarget;

    int dragY;
    private int scrollbarY, scrollYMax = 0;
    private boolean showVoterSummary = false;
    private RadialGradientPaint diploGradient;
    private Image background;
    private Empire displayedDiplomat;
    private BufferedImage diplomatImg;
    BufferedImage raceImg;
     
    public GalacticCouncilUI() {
        init0();
    }
    private void init0() {
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }
    public void init() {
        showVoterSummary = false;
        displayMode = Display.ANNOUNCE;
        background = player().council();
        startFadeTimer();
        initConsole();
    }
    public void autoRun() {
    	GalacticCouncil council = galaxy().council();
    	showVoterSummary = true;
        displayMode = Display.ANNOUNCE;
        council.continueNonPlayerVoting();
        displayMode = nextVotingMode();
        showVoterSummary = false;
        displayMode = Display.SHOW_VOTE_RESULT;
        advanceScreen();
        String s = concat(getTurn(), " | Council ");
        if(displayMode == Display.NO_WINNER) {
        	s += "No Winner";
        	advanceScreen();
        	advanceScreen();
        }
        else {
        	council.acceptRuling(player());
        	advanceScreen();
        	s += "Winner = ";
        	s += council.leader().name();
        }
        writeToFile(AUTORUN_OTHERFILE, s, true, true);
    	if (IDebugOptions.consoleAutoRun())
    		System.out.println(s);
    }
    @Override
    public String ambienceSoundKey() { return "CouncilAmbience"; }
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        super.paintComponent(g2);
        summaryBox.setBounds(0,0,0,0);
        continueBox.setBounds(0,0,0,0);
        skipBox.setBounds(0,0,0,0);
        voterListBox.setBounds(0,0,0,0);
        candidate1Box.setBounds(0,0,0,0);
        candidate2Box.setBounds(0,0,0,0);
        abstainBox.setBounds(0,0,0,0);
        acceptBox.setBounds(0,0,0,0);
        rejectBox.setBounds(0,0,0,0);

        if (showVoterSummary)
            paintVoterSummary(g2);
        else {
            switch(displayMode) {
                case ANNOUNCE:          paintStartMessage(g2);        break;
                case SHOW_VOTE_RESULT:  paintAIVoteMessage(g2);       break;
                case ASK_PLAYER_VOTE:   paintPlayerVoteMessage(g2);   break;
                case NO_WINNER:         paintNoWinnerMessage(g2);     break;
                case ACCEPT_RULING:     paintAcceptRulingMessage(g2); break;
            }
        }

        drawOverlay(g2);
    }
    private void paintStartMessage(Graphics2D g) {
        GalacticCouncil c = galaxy().council();
        Empire emp1 = c.candidate1();
        Empire emp2 = c.candidate2();
        int w = getWidth();
        int h = getHeight();
        g.drawImage(background, 0, 0, w, h, null);
        g.setColor(maskC);
        g.fillRect(0, 0, w, h);
        
        int lineTextSize = 15;
        g.setFont(narrowFont(lineTextSize));
        String text1 = text("COUNCIL_CONVENE");
        String text2 = getModedText("COUNCIL_CONVENE2");
        text2 = emp1.replaceTokens(text2, "first");
        text2 = emp2.replaceTokens(text2, "second");
        
        int bdr = s10;
        int w1 =  scaled(430);
        
        List<String> lines1 = wrappedLines(g, text1, w1-s20);
        List<String> lines2 = wrappedLines(g, text2, w1-s20);
        
        int lineH = s16;
        
        int h1a = scaled(70);
        int h1b = (lineH*(lines1.size()+lines2.size()))+s40;
        int h1c = scaled(40);  //button H
       
        int w0 = w1+bdr+bdr;
        int h0 = bdr+h1a+s5+h1b+h1c+bdr;
        int x0 = (w-w0)/2; 
        int y0 = ((h-h0)/2)-s50;
                
        int x1 = x0+bdr;
        int y1a = y0+bdr;
        int y1b = y1a+h1a+s5;
        int y1c = y0+h0-h1c;
        
        g.setColor(MainUI.paneShadeC2);
        g.fillRect(x0, y0, w0, h0);

        g.setColor(MainUI.paneBackground);
        g.fillRect(x1, y1a, w1, h1a);
        g.setColor(MainUI.paneBackground);
        g.fillRect(x1, y1b, w1, h1b);
        
        // draw year/turn info
        String yearStr = displayYearOrTurn();
        g.setFont(narrowFont(40));
        int sw = g.getFontMetrics().stringWidth(yearStr);
        int leftW = w1*4/9;
        int x1a = x1+((leftW-sw)/2);
        drawBorderedString(g, yearStr, 2, x1a, y1a+h1a-s20, SystemPanel.textShadowC, SystemPanel.orangeText);

        // draw title
        String titleStr = text("COUNCIL_CONVENE_TITLE");
        g.setFont(narrowFont(22));
        sw = g.getFontMetrics().stringWidth(titleStr);
        int rightW = w1-leftW;
        int x1b = x1+leftW+((rightW-sw)/2);
        drawShadowedString(g, titleStr, 3, x1b, y1a+h1a-s25, SystemPanel.textShadowC, SystemPanel.whiteText);

        int x2 = x1+s10;
        int y2 = y1b+s5;
        g.setFont(narrowFont(lineTextSize));
        g.setColor(SystemPanel.blackText);
        for (String line: lines1) {
            y2 += lineH;
            drawString(g,line, x2, y2);
        }
        y2 += s20;
        for (String line: lines2) {
            y2 += lineH;
            drawString(g,line, x2, y2);
        }        
        
        g.setFont(narrowFont(20));
        String button1Text = text("COUNCIL_VIEW_SUMMARY");
        String button2Text = text("COUNCIL_BEGIN_VOTING");
        int sw1 = g.getFontMetrics().stringWidth(button1Text);
        int sw2 = g.getFontMetrics().stringWidth(button2Text);
        int buttonW = max(sw1,sw2)+s40;
        int button1X = (w/2)-buttonW-s10;
        int button2X = (w/2)+s10;
        int buttonH = s30;
        int buttonY = y1c;
       
        float[] dist = {0.0f, 0.5f, 1.0f};
        Color[] colors = {greenEdgeC, greenMidC, greenEdgeC};
        
        summaryBox.setBounds(button1X, buttonY, buttonW, buttonH);
        Point2D ptStart = new Point2D.Float(button1X, 0);
        Point2D ptEnd = new Point2D.Float(button1X + buttonW, 0);
        LinearGradientPaint back1 = new LinearGradientPaint(ptStart, ptEnd, dist, colors);
        boolean hovering = hoverTarget == summaryBox;
        g.setPaint(back1);
        g.fillRoundRect(button1X, buttonY, buttonW, buttonH, s3, s3);
        Color c0 = hovering ? SystemPanel.yellowText : SystemPanel.whiteText;
        g.setColor(c0);
        Stroke prevStr = g.getStroke();
        g.setStroke(BasePanel.stroke1);
        g.drawRoundRect(button1X, buttonY, buttonW, buttonH, s3, s3);
        g.setStroke(prevStr);
        int x2a = button1X + ((buttonW - sw1) / 2);
        drawBorderedString(g, button1Text, x2a, buttonY + buttonH - s9, SystemPanel.textShadowC, c0);  
        
        continueBox.setBounds(button2X, buttonY, buttonW, buttonH);
        ptStart = new Point2D.Float(button2X, 0);
        ptEnd = new Point2D.Float(button2X + buttonW, 0);
        LinearGradientPaint back2 = new LinearGradientPaint(ptStart, ptEnd, dist, colors);
        hovering = hoverTarget == continueBox;
        g.setPaint(back2);
        g.fillRoundRect(button2X, buttonY, buttonW, buttonH, s3, s3);
        c0 = hovering ? SystemPanel.yellowText : SystemPanel.whiteText;
        g.setColor(c0);
        prevStr = g.getStroke();
        g.setStroke(BasePanel.stroke1);
        g.drawRoundRect(button2X, buttonY, buttonW, buttonH, s3, s3);
        g.setStroke(prevStr);
        int x2b = button2X + ((buttonW - sw2) / 2);
        drawBorderedString(g, button2Text, x2b, buttonY + buttonH - s9, SystemPanel.textShadowC, c0);     
    }
    private void paintAIVoteMessage(Graphics2D g) {
        GalacticCouncil c = galaxy().council();
        int w = getWidth();
        int h = getHeight();
        g.drawImage(background, 0, 0, w, h, null);
        
        drawViewSummaryButton(g);
        paintVoteTotals(g);

        String voteText;
        
        if (c.lastVoted() == null) {
            voteText = text("COUNCIL_CAST_ABSTAIN", c.lastVotes());
            voteText = c.lastVoter().replaceTokens(voteText, "voter");
        }
        else {
            voteText = text("COUNCIL_CAST_VOTE", str(c.lastVotes()));
            voteText = c.lastVoter().replaceTokens(voteText, "voter");
            voteText = c.lastVoted().replaceTokens(voteText, "candidate");
        }
        
        
        g.setFont(narrowFont(30));
        int sw1 = g.getFontMetrics().stringWidth(voteText);
        int x0 = (w-sw1)/2;
        drawBorderedString(g, voteText, 2, x0, h-s35, SystemPanel.textShadowC, SystemPanel.orangeText);
        
        String contText = text("CLICK_CONTINUE");
        g.setFont(narrowFont(16));
        g.setColor(SystemPanel.whiteText);
        sw1 = g.getFontMetrics().stringWidth(contText);
        x0 = (w-sw1)/2;
        drawString(g,contText, x0, h-s10);
        drawDiplomatImage(g, c.lastVoter());
    }
    private void paintVoteTotals(Graphics2D g) {
        GalacticCouncil c = galaxy().council();
        int h = getHeight();
        int w = getWidth();
        int barH = s90;
        float[] dist = {0.0f, 0.5f, 1.0f};
        Color[] colors = {new Color(0,0,0,0), new Color(0,0,0,180), Color.black};
        Point2D ptStart = new Point2D.Float(0, h-barH);
        Point2D ptEnd = new Point2D.Float(0, h);
        LinearGradientPaint back = new LinearGradientPaint(ptStart, ptEnd, dist, colors);
        g.setPaint(back);
        g.fillRect(0, h-barH, w, barH);
        
        // paint candidates
        int w3 = scaled(60);
        int h3 = scaled(65);
        int y3 = h-h3;
        int x3a = 0;
        int x3b = w-w3;
        
        BufferedImage img1 = c.candidate1().diploMugshotQuiet();
        BufferedImage img2 = c.candidate2().diploMugshotQuiet();

        // modnar: use (slightly) better sampling
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(img1, x3a, y3, w3, h3, null);
        g.drawImage(img2, x3b, y3, w3, h3, null);
        
        g.setColor(SystemPanel.whiteText);
        g.setFont(narrowFont(26));
        String votes1 = text("COUNCIL_VOTE_COUNT", str(c.votes1()));
        drawString(g,votes1, x3a+w3+s10, h-s20);
        
        String votes2 = text("COUNCIL_VOTE_COUNT", str(c.votes2()));
        int sw = g.getFontMetrics().stringWidth(votes2);
        drawString(g,votes2, x3b-sw-s10, h-s20);
    }
    private void paintPlayerVoteMessage(Graphics2D g) {
        GalacticCouncil c = galaxy().council();
        int w = getWidth();
        int h = getHeight();
		// modnar: use (slightly) better sampling
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(background, 0, 0, w, h, null);
        
        paintVoteTotals(g);
        g.setColor(maskC);
        g.fillRect(0, 0, w, h);
        
        int lineTextSize = 15;
        g.setFont(narrowFont(lineTextSize));
        int bdr = s10;
        int w1 =  scaled(430);
        
        int h1a = scaled(70);
        int h1b = scaled(261);
        int h1c = scaled(80);
       
        int w0 = w1+bdr+bdr;
        int h0 = bdr+h1a+s5+h1b+h1c;
        int x0 = (w-w0)/2; 
        int y0 = ((h-h0)/2)-s50;
                
        int x1 = x0+bdr;
        int y1a = y0+bdr;
        int y1b = y1a+h1a+s5;
        int y1c = y0+h0-h1c;
        
        g.setColor(MainUI.paneShadeC2);
        g.fillRect(x0, y0, w0, h0);

        g.setColor(MainUI.paneBackground);
        g.fillRect(x1, y1a, w1, h1a);
        g.setColor(MainUI.paneBackground);
        g.fillRect(x1, y1b, w1, h1b);
        
        // draw year/turn info
        String yearStr = displayYearOrTurn();
        g.setFont(narrowFont(40));
        int sw = g.getFontMetrics().stringWidth(yearStr);
        int leftW = w1/2;
        int x1a = x1+((leftW-sw)/2);
        drawBorderedString(g, yearStr, 2, x1a, y1a+h1a-s20, SystemPanel.textShadowC, SystemPanel.orangeText);

        // draw title
        int x1b = x1+leftW+s10;
        String prompt = text("COUNCIL_CAST_PROMPT", str(c.nextVotes()));
        g.setColor(SystemPanel.blackText);
        g.setFont(narrowFont(15)); 
        drawString(g,prompt, x1b, y1a+h1a-s45);
        
        String titleStr = text("COUNCIL_CAST_PROMPT_TITLE");
        g.setFont(narrowFont(22));
        drawShadowedString(g, titleStr, 3, x1b, y1a+h1a-s20, SystemPanel.textShadowC, SystemPanel.whiteText);
        
        // paint candidates
        int w3 = scaled(200);
        int h3 = scaled(216);
        int y3 = y1b+s5;
        int x3a = x1+s10;
        int x3b = x1+w1-s10-w3;
        
        // council leader images shoud be 480x216... we want to show the middle 200x216 for this screen
        BufferedImage backImg = raceBackImg();
        BufferedImage img1 = c.candidate1().councilLeader().getSubimage(140, 0, 200, 216);
        BufferedImage img2 = c.candidate2().councilLeader().getSubimage(140, 0, 200, 216);

        g.drawImage(backImg, x3a, y3, w3, h3, null);
        g.drawImage(img1, x3a, y3, w3, h3, null);
        g.drawImage(backImg, x3b, y3, w3, h3, null);
        g.drawImage(img2, x3b, y3, w3, h3, null);
        
        float[] dist = {0.0f, 0.5f, 1.0f};
        Color[] colors = {greenEdgeC, greenMidC, greenEdgeC};
        
        int button1X = x3a;
        int button2X = x3b;
        int buttonY = y3+h3+s5;
        int buttonW = w3;
        int buttonH = s30;
        
        g.setFont(narrowFont(20));
        String button1Text = c.candidate1().leader().name();
        String button2Text = c.candidate2().leader().name();
        int sw1 = g.getFontMetrics().stringWidth(button1Text);
        int sw2 = g.getFontMetrics().stringWidth(button2Text);

        candidate1Box.setBounds(button1X, buttonY, buttonW, buttonH);
        Point2D ptStart = new Point2D.Float(button1X, 0);
        Point2D ptEnd = new Point2D.Float(button1X + buttonW, 0);
        LinearGradientPaint back1 = new LinearGradientPaint(ptStart, ptEnd, dist, colors);
        boolean hovering = hoverTarget == candidate1Box;
        g.setPaint(back1);
        g.fillRoundRect(button1X, buttonY, buttonW, buttonH, s3, s3);
        Color c0 = hovering ? SystemPanel.yellowText : SystemPanel.whiteText;
        g.setColor(c0);
        Stroke prevStr = g.getStroke();
        g.setStroke(BasePanel.stroke1);
        g.drawRoundRect(button1X, buttonY, buttonW, buttonH, s3, s3);
        g.setStroke(prevStr);
        int x2a = button1X + ((buttonW - sw1) / 2);
        drawBorderedString(g, button1Text, x2a, buttonY + buttonH - s9, SystemPanel.textShadowC, c0);  
        
        candidate2Box.setBounds(button2X, buttonY, buttonW, buttonH);
        ptStart = new Point2D.Float(button2X, 0);
        ptEnd = new Point2D.Float(button2X + buttonW, 0);
        LinearGradientPaint back2 = new LinearGradientPaint(ptStart, ptEnd, dist, colors);
        hovering = hoverTarget == candidate2Box;
        g.setPaint(back2);
        g.fillRoundRect(button2X, buttonY, buttonW, buttonH, s3, s3);
        c0 = hovering ? SystemPanel.yellowText : SystemPanel.whiteText;
        g.setColor(c0);
        prevStr = g.getStroke();
        g.setStroke(BasePanel.stroke1);
        g.drawRoundRect(button2X, buttonY, buttonW, buttonH, s3, s3);
        g.setStroke(prevStr);
        int x2b = button2X + ((buttonW - sw2) / 2);
        drawBorderedString(g, button2Text, x2b, buttonY + buttonH - s9, SystemPanel.textShadowC, c0);    
        
        Color[] colors2 = {grayEdgeC, grayMidC, grayEdgeC};
        int button3X = (w-buttonW)/2;
        int button3Y = y1c+s5;
        int button4Y = button3Y+buttonH+s5;
        String button3Text = text("COUNCIL_CHOICE_ABSTAIN");
        String button4Text = text("COUNCIL_VIEW_SUMMARY");
        int sw3 = g.getFontMetrics().stringWidth(button3Text);
        int sw4 = g.getFontMetrics().stringWidth(button4Text);

        abstainBox.setBounds(button3X, button3Y, buttonW, buttonH);
        ptStart = new Point2D.Float(button3X, 0);
        ptEnd = new Point2D.Float(button3X + buttonW, 0);
        LinearGradientPaint back3 = new LinearGradientPaint(ptStart, ptEnd, dist, colors2);
        hovering = hoverTarget == abstainBox;
        g.setPaint(back3);
        g.fillRoundRect(button3X, button3Y, buttonW, buttonH, s3, s3);
        c0 = hovering ? SystemPanel.yellowText : SystemPanel.whiteText;
        g.setColor(c0);
        prevStr = g.getStroke();
        g.setStroke(BasePanel.stroke1);
        g.drawRoundRect(button3X, button3Y, buttonW, buttonH, s3, s3);
        g.setStroke(prevStr);
        int x4a = button3X + ((buttonW - sw3) / 2);
        drawBorderedString(g, button3Text, x4a, button3Y + buttonH - s9, SystemPanel.textShadowC, c0);  

        summaryBox.setBounds(button3X, button4Y, buttonW, buttonH);
        ptStart = new Point2D.Float(button3X, 0);
        ptEnd = new Point2D.Float(button3X + buttonW, 0);
        LinearGradientPaint back4 = new LinearGradientPaint(ptStart, ptEnd, dist, colors2);
        hovering = hoverTarget == summaryBox;
        g.setPaint(back4);
        g.fillRoundRect(button3X, button4Y, buttonW, buttonH, s3, s3);
        c0 = hovering ? SystemPanel.yellowText : SystemPanel.whiteText;
        g.setColor(c0);
        prevStr = g.getStroke();
        g.setStroke(BasePanel.stroke1);
        g.drawRoundRect(button3X, button4Y, buttonW, buttonH, s3, s3);
        g.setStroke(prevStr);
        int x4b = button3X + ((buttonW - sw4) / 2);
        drawBorderedString(g, button4Text, x4b, button4Y + buttonH - s9, SystemPanel.textShadowC, c0);  
    }
    private String getModedText(String key)	{
    	float pctRequired = GalacticCouncil.pctRequired() * 100;
    	boolean isTwoThird = 2000 == round(pctRequired * 30);
    	String text;
    	if (isTwoThird)
    		text = text(key);
    	else
    		text = text(key+"_MOD", df1.format(pctRequired));
    	return text;
    }
    private void paintNoWinnerMessage(Graphics2D g) {
        int w = getWidth();
        int h = getHeight();
		// modnar: use (slightly) better sampling
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(background, 0, 0, w, h, null);
        paintVoteTotals(g);
        g.setColor(maskC);
        g.fillRect(0, 0, w, h);
        
        int lineTextSize = 15;
        g.setFont(narrowFont(lineTextSize));
        String text1 = getModedText("COUNCIL_ADJOURN");
        
        int bdr = s10;
        int w1 =  scaled(430);
        
        List<String> lines1 = wrappedLines(g, text1, w1-s20);
        
        int lineH = s16;
        
        int h1a = scaled(70);
        int h1b = (lineH*lines1.size())+s20;
        int h1c = scaled(40);  //button H
       
        int w0 = w1+bdr+bdr;
        int h0 = bdr+h1a+s5+h1b+h1c+bdr;
        int x0 = (w-w0)/2; 
        int y0 = ((h-h0)/2)-s50;
                
        int x1 = x0+bdr;
        int y1a = y0+bdr;
        int y1b = y1a+h1a+s5;
        int y1c = y0+h0-h1c;
        
        g.setColor(MainUI.paneShadeC2);
        g.fillRect(x0, y0, w0, h0);

        g.setColor(MainUI.paneBackground);
        g.fillRect(x1, y1a, w1, h1a);
        g.setColor(MainUI.paneBackground);
        g.fillRect(x1, y1b, w1, h1b);
        
        
        // draw year/turn info
        String yearStr = displayYearOrTurn();
        g.setFont(narrowFont(40));
        int sw = g.getFontMetrics().stringWidth(yearStr);
        int leftW = w1*4/9;
        int x1a = x1+((leftW-sw)/2);
        drawBorderedString(g, yearStr, 2, x1a, y1a+h1a-s20, SystemPanel.textShadowC, SystemPanel.orangeText);

        // draw title
        String titleStr = text("COUNCIL_ADJOURN_TITLE");
        g.setFont(narrowFont(22));
        sw = g.getFontMetrics().stringWidth(titleStr);
        int rightW = w1-leftW;
        int x1b = x1+leftW+((rightW-sw)/2);
        drawShadowedString(g, titleStr, 3, x1b, y1a+h1a-s25, SystemPanel.textShadowC, SystemPanel.whiteText);

        int x2 = x1+s10;
        int y2 = y1b+s5;
        g.setFont(narrowFont(lineTextSize));
        g.setColor(SystemPanel.blackText);
        for (String line: lines1) {
            y2 += lineH;
            drawString(g,line, x2, y2);
        } 
        
        g.setFont(narrowFont(20));
        String button1Text = text("COUNCIL_CONTINUE");
        int sw1 = g.getFontMetrics().stringWidth(button1Text);
        int buttonW = sw1+s40;
        int button1X = (w-buttonW)/2;
        int buttonH = s30;
        int buttonY = y1c;
       
        float[] dist = {0.0f, 0.5f, 1.0f};
        Color[] colors = {greenEdgeC, greenMidC, greenEdgeC};
        
        continueBox.setBounds(button1X, buttonY, buttonW, buttonH);
        Point2D ptStart = new Point2D.Float(button1X, 0);
        Point2D ptEnd = new Point2D.Float(button1X + buttonW, 0);
        LinearGradientPaint back1 = new LinearGradientPaint(ptStart, ptEnd, dist, colors);
        boolean hovering = hoverTarget == continueBox;
        g.setPaint(back1);
        g.fillRoundRect(button1X, buttonY, buttonW, buttonH, s3, s3);
        Color c0 = hovering ? SystemPanel.yellowText : SystemPanel.whiteText;
        g.setColor(c0);
        Stroke prevStr = g.getStroke();
        g.setStroke(BasePanel.stroke1);
        g.drawRoundRect(button1X, buttonY, buttonW, buttonH, s3, s3);
        g.setStroke(prevStr);
        int x2a = button1X + ((buttonW - sw1) / 2);
        drawBorderedString(g, button1Text, x2a, buttonY + buttonH - s9, SystemPanel.textShadowC, c0);  
    }
    private void paintAcceptRulingMessage(Graphics2D g) {
        GalacticCouncil c = galaxy().council();
        int w = getWidth();
        int h = getHeight();
		// modnar: use (slightly) better sampling
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(background, 0, 0, w, h, null);
        
        paintVoteTotals(g);
        g.setColor(maskC);
        g.fillRect(0, 0, w, h);
        
        int lineTextSize = 15;
        g.setFont(narrowFont(lineTextSize));
        int bdr = s10;
        int w1 =  scaled(500);
        
        int h1a = scaled(70);
        int h1b = scaled(226);
        int h1c = scaled(50);
       
        int w0 = w1+bdr+bdr;
        int h0 = bdr+h1a+s5+h1b+h1c;
        int x0 = (w-w0)/2; 
        int y0 = ((h-h0)/2)-s50;
                
        int x1 = x0+bdr;
        int y1a = y0+bdr;
        int y1b = y1a+h1a+s5;
        int y1c = y0+h0-h1c;
        
        g.setColor(MainUI.paneShadeC2);
        g.fillRect(x0, y0, w0, h0);

        g.setColor(MainUI.paneBackground);
        g.fillRect(x1, y1a, w1, h1a);
        g.setColor(MainUI.paneBackground);
        g.fillRect(x1, y1b, w1, h1b);
        
        // draw year/turn info
        String yearStr = displayYearOrTurn();
        g.setFont(narrowFont(40));
        int sw = g.getFontMetrics().stringWidth(yearStr);
        int leftW = w1*4/9;
        int x1a = x1+((leftW-sw)/2);
        drawBorderedString(g, yearStr, 2, x1a, y1a+h1a-s20, SystemPanel.textShadowC, SystemPanel.orangeText);

        // draw title
        int x1b = x1+leftW+s5;
        String titleStr = text("COUNCIL_ELECTED_TITLE");
        g.setFont(narrowFont(26));
        drawShadowedString(g, titleStr, 3, x1b, y1a+h1a-s20, SystemPanel.textShadowC, SystemPanel.whiteText);
        
        // paint candidates
        int w3 = scaled(480);
        int h3 = scaled(216);
        int y3 = y1b+s5;
        int x3a = x1+s10;
       
        BufferedImage backImg = wideBackImg();
        BufferedImage img1 = c.leader().councilLeader();

        g.drawImage(backImg, x3a, y3, w3, h3, null);
        g.drawImage(img1, x3a, y3, w3, h3, null);
        
        g.setFont(narrowFont(20));
        String button1Text = text("COUNCIL_ACCEPT_RULING");
        String button2Text = text("COUNCIL_REJECT_RULING");
        int sw1 = g.getFontMetrics().stringWidth(button1Text);
        int sw2 = g.getFontMetrics().stringWidth(button2Text);
        int buttonW = max(sw1,sw2)+s40;
        int button1X = (w/2)-buttonW-s10;
        if (options().realmsBeyondCouncil()) {
        	button1X = (w - buttonW)/2;
        }
        int button2X = (w/2)+s10;
        int buttonH = s30;
        int buttonY = y1c+s10;
       
        float[] dist = {0.0f, 0.5f, 1.0f};
        Color[] colors = {greenEdgeC, greenMidC, greenEdgeC};
        Color[] colors2 = {redEdgeC, redMidC, redEdgeC};
        
        acceptBox.setBounds(button1X, buttonY, buttonW, buttonH);
        Point2D ptStart = new Point2D.Float(button1X, 0);
        Point2D ptEnd = new Point2D.Float(button1X + buttonW, 0);
        LinearGradientPaint back1 = new LinearGradientPaint(ptStart, ptEnd, dist, colors);
        boolean hovering = hoverTarget == acceptBox;
        g.setPaint(back1);
        g.fillRoundRect(button1X, buttonY, buttonW, buttonH, s3, s3);
        Color c0 = hovering ? SystemPanel.yellowText : SystemPanel.whiteText;
        g.setColor(c0);
        Stroke prevStr = g.getStroke();
        g.setStroke(BasePanel.stroke1);
        g.drawRoundRect(button1X, buttonY, buttonW, buttonH, s3, s3);
        g.setStroke(prevStr);
        int x2a = button1X + ((buttonW - sw1) / 2);
        drawBorderedString(g, button1Text, x2a, buttonY + buttonH - s9, SystemPanel.textShadowC, c0);  
        
        if (!options().realmsBeyondCouncil()) {
        	rejectBox.setBounds(button2X, buttonY, buttonW, buttonH);
            ptStart = new Point2D.Float(button2X, 0);
            ptEnd = new Point2D.Float(button2X + buttonW, 0);
            LinearGradientPaint back2 = new LinearGradientPaint(ptStart, ptEnd, dist, colors2);
            hovering = hoverTarget == rejectBox;
            g.setPaint(back2);
            g.fillRoundRect(button2X, buttonY, buttonW, buttonH, s3, s3);
            c0 = hovering ? SystemPanel.yellowText : SystemPanel.whiteText;
            g.setColor(c0);
            prevStr = g.getStroke();
            g.setStroke(BasePanel.stroke1);
            g.drawRoundRect(button2X, buttonY, buttonW, buttonH, s3, s3);
            g.setStroke(prevStr);
            int x2b = button2X + ((buttonW - sw2) / 2);
           	drawBorderedString(g, button2Text, x2b, buttonY + buttonH - s9, SystemPanel.textShadowC, c0);       
        }
    }
    private void paintVoterSummary(Graphics2D g) {
        GalacticCouncil c = galaxy().council();
        Empire pl = player();
        int w = getWidth();
        int h = getHeight();
		// modnar: use (slightly) better sampling
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(background, 0, 0, w, h, null);
        
        paintVoteTotals(g);
        g.setColor(maskC);
        g.fillRect(0, 0, w, h);      
        
        int MAX_ROWS = 4;
        List<Empire> voters = c.voters();
        int rowsNeeded = (voters.size()+1)/2;
        boolean needsScroll = rowsNeeded > MAX_ROWS;
        
        int empireBoxW = scaled(280);
        int empireBoxH = s95;
        int empireBoxSpace = s7;
        int rows = min(MAX_ROWS, rowsNeeded);
        int listH = (rows*empireBoxH)+((rows-1)*empireBoxSpace);
        
        int scrollW = s12;
        int w0= scaled(585);
        if (needsScroll)
            w0 += scrollW;
        int h0= listH+scaled(135);
        int x0= (w-w0)/2;
        int y0= scaled(270)-(rows*s50);
        
        int w1 = scaled(565);
        int h1 = s75;
        int x1 = x0+s10;
        int y1 = y0+s7;
        
        int w2 = w1;
        int x2 = x1;
        int h2 = listH;
        int y2 = y1+h1+s10;
        
        g.setColor(MainUI.paneShadeC2);
        g.fillRect(x0, y0, w0, h0);

        g.setColor(MainUI.paneBackground);
        g.fillRect(x1, y1, w1, h1);
        
        // draw year/turn info
        String yearStr = displayYearOrTurn();
        g.setFont(narrowFont(40));
        int sw = g.getFontMetrics().stringWidth(yearStr);
        int leftW = w1/2;
        int x1a = x1+((leftW-sw)/2);
        drawBorderedString(g, yearStr, 2, x1a, y1+h1-s25, SystemPanel.textShadowC, SystemPanel.orangeText);

        // draw title
        int x1b = x1+leftW+s10;
        g.setColor(SystemPanel.blackText);
        g.setFont(narrowFont(15)); 
        
        String prompt = text("COUNCIL_SUMMARY", str(c.votesToElect()));
        List<String> lines = wrappedLines(g, prompt, w1-leftW-s20);
        int y1b = y1+h1-s42-(s8*lines.size());
        
        for (String line: lines) {
            drawString(g,line, x1b, y1b);
            y1b += s16;
        }
        
        y1b += s15;
        String titleStr = text("COUNCIL_SUMMARY_TITLE");
        g.setFont(narrowFont(26));
        drawShadowedString(g, titleStr, 3, x1b, y1b, SystemPanel.textShadowC, SystemPanel.whiteText);
        
        g.setClip(x2,y2,w2,h2);
        int y2a = needsScroll ? y2-scrollbarY : y2;
        int fullListH = 0;
        for (int i=0;i<voters.size();i++) {
            Empire voter = voters.get(i);
            int x2a = i%2==0 ? x2 : x2+empireBoxW+s5;
            drawVoterSummaryPane(g, voter, x2a, y2a, empireBoxW, empireBoxH);
            if (i%2 ==1) 
                y2a = y2a+empireBoxH+empireBoxSpace;
            else
                fullListH = fullListH+empireBoxH+empireBoxSpace;
        }
        g.setClip(null);
        voterListBox.setBounds(x1,y2,w1,h2);
        scrollYMax = max(0, fullListH-listH);
       
        if (scrollYMax == 0)
            scrollbar.setBounds(0,0,0,0);
        else if (needsScroll) {
            g.setColor(scrollBarC);
            int scrollH = (int) ((float)listH*listH/(listH+scrollYMax));
            int scrollX = x0+w0-scrollW-s6;
            int scrollY =(int) (y2+ (float)listH*scrollbarY/(scrollYMax+listH));
            g.fillRoundRect(scrollX, scrollY, scrollW, scrollH, s4, s4);
            scrollbar.setBounds(scrollX, scrollY, scrollW, scrollH);
            if (hoverTarget == scrollbar) {
                Stroke prev = g.getStroke();
                g.setColor(Color.yellow);
                g.setStroke(stroke2);
                g.drawRoundRect(scrollX, scrollY, scrollW, scrollH, s4, s4);
                g.setStroke(prev);
            }
        }
        
        g.setFont(narrowFont(20));
        int buttonH = s30;
        int buttonY = y2+h2+s7;       
        float[] dist = {0.0f, 0.5f, 1.0f};
        Color[] colors = {greenEdgeC, greenMidC, greenEdgeC};
        
        String button1Text = c.votingInProgress() ? text("COUNCIL_SHOW_VOTING") : text("COUNCIL_SHOW_RESULTS");
        int sw1 = g.getFontMetrics().stringWidth(button1Text);
        int button1W = sw1+s40;
        
        int gap = (w1-button1W)/2;
        
        String button2Text = null;
        int button2W = 0;
        int button2X = x1;
        int sw2 = 0;
        if  (c.votingInProgress()) {
            if (c.hasVoted(pl) || options().isAutoPlay())
                button2Text = text("COUNCIL_COMPLETE_VOTING");
            else {
                button2Text = text("COUNCIL_SKIP_TO_PLAYER");
                button2Text = pl.replaceTokens(button2Text, "player");
            }
            sw2 = g.getFontMetrics().stringWidth(button2Text);
            button2W = sw2+s40;
            gap = (w1-button1W-button2W)/3;
        }
        
        if (c.votingInProgress()) {
            button2X = x1+gap;
            skipBox.setBounds(button2X, buttonY, button2W, buttonH);
            Point2D ptStart = new Point2D.Float(button2X, 0);
            Point2D ptEnd = new Point2D.Float(button2X + button2W, 0);
            LinearGradientPaint back2 = new LinearGradientPaint(ptStart, ptEnd, dist, colors);
            boolean hovering = hoverTarget == skipBox;
            g.setPaint(back2);
            g.fillRoundRect(button2X, buttonY, button2W, buttonH, s3, s3);
            Color c0 = hovering ? SystemPanel.yellowText : SystemPanel.whiteText;
            g.setColor(c0);
            Stroke prevStr = g.getStroke();
            g.setStroke(BasePanel.stroke1);
            g.drawRoundRect(button2X, buttonY, button2W, buttonH, s3, s3);
            g.setStroke(prevStr);
            int x2a = button2X + ((button2W - sw2) / 2);
            drawBorderedString(g, button2Text, x2a, buttonY + buttonH - s9, SystemPanel.textShadowC, c0);          
        }

        int button1X = button2X+button2W+gap;   
        continueBox.setBounds(button1X, buttonY, button1W, buttonH);
        Point2D ptStart = new Point2D.Float(button1X, 0);
        Point2D ptEnd = new Point2D.Float(button1X + button1W, 0);
        LinearGradientPaint back1 = new LinearGradientPaint(ptStart, ptEnd, dist, colors);
        boolean hovering = hoverTarget == continueBox;
        g.setPaint(back1);
        g.fillRoundRect(button1X, buttonY, button1W, buttonH, s3, s3);
        Color c0 = hovering ? SystemPanel.yellowText : SystemPanel.whiteText;
        g.setColor(c0);
        Stroke prevStr = g.getStroke();
        g.setStroke(BasePanel.stroke1);
        g.drawRoundRect(button1X, buttonY, button1W, buttonH, s3, s3);
        g.setStroke(prevStr);
        int x2a = button1X + ((button1W - sw1) / 2);
        drawBorderedString(g, button1Text, x2a, buttonY + buttonH - s9, SystemPanel.textShadowC, c0);          
    }
    private void drawVoterSummaryPane(Graphics2D g, Empire voter, int x, int y, int w, int h) {
        GalacticCouncil c = galaxy().council();
        g.setColor(MainUI.paneBackground);
        g.fillRect(x, y, w, h);
        
		// modnar: use (slightly) better sampling
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        BufferedImage img = iconBackImg();
        g.drawImage(img, x, y, this);
        int imgW = img.getWidth();
        int imgH = img.getHeight();
        
        BufferedImage img2 = voter.diploMugshotQuiet();
        g.drawImage(img2, x,y,imgW, imgH, null);
        
        boolean voted = c.hasVoted(voter);
        int votes = c.votes(voter);
        String voteStr;
        if (voted) {
            Empire votee = galaxy().empire(voter.lastCouncilVoteEmpId());
            if (votee == null)
                voteStr = text("COUNCIL_VOTE_ABSTAINED", str(votes));
            else {
                voteStr = text("COUNCIL_VOTE_CAST", str(votes));
                voteStr = votee.replaceTokens(voteStr, "alien");
            }
        }
        else
            voteStr = text("COUNCIL_VOTE_COUNT", str(votes));
        String cand1Str = treatyString(voter, c.candidate1());
        String cand2Str = treatyString(voter, c.candidate2());
        
        int x1 = x+imgW+s10;
        g.setFont(narrowFont(24));
        drawShadowedString(g, voter.raceName(), 2, x1, y+s30, SystemPanel.textShadowC, SystemPanel.whiteText);
        g.setFont(narrowFont(16));
        g.setColor(Color.black);
        drawString(g,voteStr, x1, y+s47);
        g.setFont(narrowFont(14));
        g.setColor(SystemPanel.blackText);
        drawString(g,cand1Str, x1, y+s65);
        drawString(g,cand2Str, x1, y+s80);
    }
    private String treatyString(Empire voter, Empire candidate) {
        String treatyStr;
        if (voter == candidate)
            treatyStr = text("COUNCIL_TREATY_CANDIDATE");
        else if (voter.alliedWith(candidate.id))
            treatyStr = text("COUNCIL_TREATY_ALLIANCE");
        else if (voter.pactWith(candidate.id))
            treatyStr = text("COUNCIL_TREATY_PACT");
        else if (voter.atWarWith(candidate.id))
            treatyStr = text("COUNCIL_TREATY_WAR");
        else
            treatyStr = text("COUNCIL_TREATY_NONE");

        treatyStr = candidate.replaceTokens(treatyStr, "alien");
        return treatyStr;
    }
    private void drawViewSummaryButton(Graphics2D g) {
        g.setFont(narrowFont(20));
        String button1Text = text("COUNCIL_VIEW_SUMMARY");
        int sw1 = g.getFontMetrics().stringWidth(button1Text);
        int buttonW = sw1+s40;
        int button1X = s10;
        int buttonH = s30;
        int buttonY = s20;
       
        float[] dist = {0.0f, 0.5f, 1.0f};
        Color[] colors = {grayEdgeC, grayMidC, grayEdgeC};
        
        summaryBox.setBounds(button1X, buttonY, buttonW, buttonH);
        Point2D ptStart = new Point2D.Float(button1X, 0);
        Point2D ptEnd = new Point2D.Float(button1X + buttonW, 0);
        LinearGradientPaint back1 = new LinearGradientPaint(ptStart, ptEnd, dist, colors);
        boolean hovering = hoverTarget == summaryBox;
        g.setPaint(back1);
        g.fillRoundRect(button1X, buttonY, buttonW, buttonH, s3, s3);
        Color c0 = hovering ? SystemPanel.yellowText : SystemPanel.whiteText;
        g.setColor(c0);
        Stroke prevStr = g.getStroke();
        g.setStroke(BasePanel.stroke1);
        g.drawRoundRect(button1X, buttonY, buttonW, buttonH, s3, s3);
        g.setStroke(prevStr);
        int x2a = button1X + ((buttonW - sw1) / 2);
        drawBorderedString(g, button1Text, x2a, buttonY + buttonH - s9, SystemPanel.textShadowC, c0);  
    }
    private void drawDiplomatImage(Graphics2D g, Empire emp) {
        if (emp == null)
            return;

        if (emp != displayedDiplomat) {
            displayedDiplomat = emp;
            int h = getHeight() * 2/5;
            int w = h;
            diplomatImg = newBufferedImage(w, h);
            Graphics2D imgG =  (Graphics2D) diplomatImg.getGraphics();
			// modnar: use (slightly) better sampling
			imgG.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			imgG.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // draw race diplo image
            BufferedImage dipImg = emp.diplomatQuiet();
            int h0 = dipImg.getHeight();
            int w0 = diplomatImg.getWidth()*h0/diplomatImg.getHeight();
            Composite prevComp  =  imgG.getComposite();
            if (player().diploOpacity() < 1) {
                AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)player().diploOpacity());
                imgG.setComposite(ac);
            }
            imgG.drawImage(dipImg, 0, 0, w, h, 0, 0, w0, h0, null);
            int x = w/2;
            int y = h/3;
            int r = scaled(300);
            imgG.setPaint(diploGradient(x,y,r));
            imgG.setComposite(AlphaComposite.DstOut);
            imgG.fillOval(x-r, y-r, r*2, r*2);
            imgG.setPaint(null);
            imgG.setComposite(prevComp);
            imgG.dispose();
        }
        int h = getHeight();
        int w = getWidth();
        int h1 = (int) (h*player().diploScale());
        int w1 = diplomatImg.getWidth()*h1/diplomatImg.getHeight();
        int xOff = scaled(player().diploXOffset());
        int yOff = scaled(player().diploYOffset());
		// modnar: use (slightly) better sampling
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(diplomatImg, ((w-w1)/2)+xOff, ((h-h1)/2)+yOff, w1, h1, null);
    }
    public BufferedImage wideBackImg() {
        if (wideBackImg == null)
            initWideBackImg();
        return wideBackImg;
    }
    public BufferedImage raceBackImg() {
        if (raceBackImg == null)
            initRaceBackImg();
        return raceBackImg;
    }
    public BufferedImage iconBackImg() {
        if (iconBackImg == null)
            initIconBackImg();
        return iconBackImg;
    }
    private void initIconBackImg() {
        int w = s88;
        int h = s95;
        iconBackImg = gc().createCompatibleImage(w, h);

        Point2D center = new Point2D.Float(w/2, h/2);
        float radius = scaled(150);
        float[] dist = {0.0f, 0.1f, 0.5f, 1.0f};
        Color[] colors = {raceCenterColor, raceCenterColor, raceEdgeColor, raceEdgeColor};
        RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
        Graphics2D g = (Graphics2D) iconBackImg.getGraphics();
        g.setPaint(p);
        g.fillRect(0, 0, w, h);
        g.dispose();
    }
    private void initRaceBackImg() {
        int w = scaled(200);
        int h = scaled(216);
        raceBackImg = gc().createCompatibleImage(w, h);

        Point2D center = new Point2D.Float(w/2, h/2);
        float radius = h;
        float[] dist = {0.0f, 0.1f, 0.5f, 1.0f};
        Color[] colors = {raceCenterColor, raceCenterColor, raceEdgeColor, raceEdgeColor};
        RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
        Graphics2D g = (Graphics2D) raceBackImg.getGraphics();
        g.setPaint(p);
        g.fillRect(0, 0, w, h);
        g.dispose();
    }
    private void initWideBackImg() {
        int w = scaled(480);
        int h = scaled(216);
        wideBackImg = gc().createCompatibleImage(w, h);

        Point2D center = new Point2D.Float(w/2, h/2);
        float radius = w;
        float[] dist = {0.0f, 0.1f, 0.5f, 1.0f};
        Color[] colors = {raceCenterColor, raceCenterColor, raceEdgeColor, raceEdgeColor};
        RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
        Graphics2D g = (Graphics2D) wideBackImg.getGraphics();
        g.setPaint(p);
        g.fillRect(0, 0, w, h);
        g.dispose();
    }
    private void advanceScreen() {
        if (stillFading())
            return;

        switch(displayMode) {
            case ANNOUNCE:
                displayMode = nextVotingMode();
                break;
            case SHOW_VOTE_RESULT:
                if (galaxy().council().votingInProgress())
                    displayMode = nextVotingMode();
                else if (galaxy().council().hasLeader()) {
                	displayMode = Display.ACCEPT_RULING;
//                	if (options().realmsBeyondCouncil()) {
//                		galaxy().council().acceptRuling(player());
//                		exit();
//                		break;
//                	}
                }
                else
                    displayMode = Display.NO_WINNER;
                break;
            case ASK_PLAYER_VOTE:
                displayMode = Display.SHOW_VOTE_RESULT;
                break;
            case NO_WINNER:
            case ACCEPT_RULING:
                exit();
                break;
        }
        initConsole();
        repaint();
    }
    private void exit() {
        repaint();
        session().resumeNextTurnProcessing();
    }
    private Display nextVotingMode() {
        GalacticCouncil c = galaxy().council();
        if (!c.votingInProgress())
            return Display.SHOW_VOTE_RESULT;
        
        if (c.nextVoter().isPlayerControlled())
            return Display.ASK_PLAYER_VOTE;

        galaxy().council().castNextVote();
        return Display.SHOW_VOTE_RESULT;
    }
    private RadialGradientPaint diploGradient(int x, int y, int r) {
        if (diploGradient == null) {
            diploGradient = new RadialGradientPaint(
                            new Point2D.Float(x,y),
                            r,
                            new float[]{0.0f, 0.2f, 0.6f, 1.0f},
                            new Color[] { newColor(0,0,0,0), newColor(0,0,0,0), newColor(0,0,0,255), newColor(0,0,0,255) }
            );
        }
        return diploGradient;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        GalacticCouncil c = galaxy().council();
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_L && e.isAltDown()) {
        	debugReloadLabels(this);
        	return;
        }
        // no key presses on screens where player selection is required
        switch(displayMode) {
            case ASK_PLAYER_VOTE:
                switch(k) {
                    case KeyEvent.VK_1: c.castPlayerVote(c.candidate1()); break;
                    case KeyEvent.VK_2: c.castPlayerVote(c.candidate2()); break;
                    case KeyEvent.VK_3: c.castPlayerVote(null); break;
                    default: return; // don't advance screen if no vote
                }
                advanceScreen();
                return;
            case ACCEPT_RULING:
                switch(k) {
                    case KeyEvent.VK_1: c.acceptRuling(player()); break;
                    case KeyEvent.VK_2:
                    	if (options().realmsBeyondCouncil())
                    		return;
                    	c.defyRuling(player());
                    	break;
                    default: return; // don't advance screen if no vote
                }
                advanceScreen();
                return;
            default:
                switch(k) {
                    case KeyEvent.VK_ESCAPE:
                    case KeyEvent.VK_1:
                        advanceScreen();
                        return;
                }
        }
    }
    @Override
    public void animate() {
        if (stillFading()) {
            advanceFade();
            repaint();
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) {
        if (hoverTarget != null) {
            hoverTarget = null;
            repaint();
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        dragY = e.getY();
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        dragY = 0;
        if ((e.getButton() > 3) || e.getClickCount() > 1)
            return;

        GalacticCouncil c = galaxy().council();
        if (showVoterSummary) {
            if (hoverTarget == continueBox) {
                showVoterSummary = false;
                if (!c.votingInProgress()) {
                    displayMode = Display.SHOW_VOTE_RESULT;
                    advanceScreen();
                }
                else if (displayMode == Display.ANNOUNCE)
                    displayMode = nextVotingMode();
                repaint();
                return;
            }
            else if (hoverTarget == skipBox) {
                c.continueNonPlayerVoting();
                if (displayMode == Display.ANNOUNCE)
                    displayMode = nextVotingMode();
                if (displayMode == Display.ASK_PLAYER_VOTE)
                    showVoterSummary = false;
                repaint();
                return;
            }
        }
        else if (!showVoterSummary && (hoverTarget == summaryBox) ) {
            scrollbarY = 0;
            showVoterSummary = true;
            repaint();
            return;
        }
        // fall out for modes that do not ask for choices
        switch(displayMode) {
            case ANNOUNCE:
            case NO_WINNER:
                if (hoverTarget == continueBox)
                    advanceScreen();
                return;
            case SHOW_VOTE_RESULT:
                if (hoverTarget == null)
                    advanceScreen();
                return;
            case ASK_PLAYER_VOTE:
                if (hoverTarget == candidate1Box) 
                    c.castPlayerVote(c.candidate1());
                else if (hoverTarget == candidate2Box)
                    c.castPlayerVote(c.candidate2());
                else if (hoverTarget == abstainBox)
                    c.castPlayerVote(null);
                else
                    return;
                advanceScreen();
                return;
            case ACCEPT_RULING:
                if (hoverTarget == acceptBox) 
                    c.acceptRuling(player());
                else if (hoverTarget == rejectBox)
                    c.defyRuling(player());
                else
                    return;
                advanceScreen();
                return;
        }
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        //int x = e.getX();
        int y = e.getY();
        int dY = y-dragY;
        dragY = y;
        if (scrollbar == hoverTarget) {
            if ((y >= voterListBox.y) || (y <= (voterListBox.y+voterListBox.height))) { 
                int h = (int) voterListBox.getHeight();
                int dListY = (int)((float)dY*(h+scrollYMax)/h);
                if (dY < 0)
                    scrollbarY = max(0,scrollbarY+dListY);
                else 
                    scrollbarY = min(scrollYMax,scrollbarY+dListY);
            }
            repaint();
            return;
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        Shape prevHover = hoverTarget;
        hoverTarget = null;
        if (continueBox.contains(x,y)) 
            hoverTarget = continueBox;
        else if (skipBox.contains(x,y))  
            hoverTarget = skipBox;      
        else if (summaryBox.contains(x,y))  
            hoverTarget = summaryBox;      
        else if (candidate1Box.contains(x,y))  
            hoverTarget = candidate1Box;      
        else if (candidate2Box.contains(x,y))  
            hoverTarget = candidate2Box;      
        else if (abstainBox.contains(x,y))   
            hoverTarget = abstainBox;      
        else if (acceptBox.contains(x,y))  
            hoverTarget = acceptBox;      
        else if (rejectBox.contains(x,y))   
            hoverTarget = rejectBox;      
        else if (scrollbar.contains(x,y))
            hoverTarget = scrollbar;
        else if (voterListBox.contains(x,y))
            hoverTarget = voterListBox;
   
        if (hoverTarget != prevHover) {
            repaint();
            return;
        }
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int count = e.getUnitsToScroll();
        if ((hoverTarget == voterListBox)
        || (hoverTarget == scrollbar)) {
            int prevY = scrollbarY;
            if (count < 0)
                scrollbarY = max(0,scrollbarY-s10);
            else 
                scrollbarY = min(scrollYMax,scrollbarY+s10);
            if (scrollbarY != prevY)
                repaint();
            return;
        }
    }

    // ##### Console Tools
    @Override public boolean handleKeyPress(KeyEvent e)	{
    	GalacticCouncil c = galaxy().council();
    	int k = e.getKeyCode();
    	if (k == KeyEvent.VK_L && e.isAltDown()) {
    		debugReloadLabels(this);
    		return false; // don't advance screen if no vote
    	}
    	if (showVoterSummary) {
            switch(k) {
            case KeyEvent.VK_ESCAPE:
            case KeyEvent.VK_1:
            	showVoterSummary = false;
                if (!c.votingInProgress()) {
                    displayMode = Display.SHOW_VOTE_RESULT;
                    advanceScreen();
                }
                else if (displayMode == Display.ANNOUNCE) {
                	displayMode = nextVotingMode();
                    initConsole();
                }
                initConsole();

                repaint();
                return true;
            case KeyEvent.VK_2:
            	 c.continueNonPlayerVoting();
                 if (displayMode == Display.ANNOUNCE)
                     displayMode = nextVotingMode();
                 if (displayMode == Display.ASK_PLAYER_VOTE)
                     showVoterSummary = false;
                 initConsole();
                 repaint();
                 return true;
            default:
            	return false; // don't advance screen if no vote
            }
        }
        else if (displayMode == Display.ASK_PLAYER_VOTE && k == KeyEvent.VK_4) {
            scrollbarY = 0;
            showVoterSummary = true;
            initConsole();
            repaint();
            return true;
        }
    	else if (displayMode == Display.ANNOUNCE) {
            switch(k) {
            case KeyEvent.VK_ESCAPE:
            case KeyEvent.VK_1:
            	scrollbarY = 0;
                showVoterSummary = true;
                initConsole();
                repaint();
            	return true;
            case KeyEvent.VK_2:
            	c.continueNonPlayerVoting();
            	advanceScreen();
            	return true;
            default:
            	return false; // don't advance screen if no vote
            }
    	}
    	else if (displayMode == Display.SHOW_VOTE_RESULT) {
            switch(k) {
            case KeyEvent.VK_ESCAPE:
            case KeyEvent.VK_1:
            	advanceScreen();
                return true;
            case KeyEvent.VK_2:
            	c.continueNonPlayerVoting();
            	advanceScreen();
            	return true;
             case KeyEvent.VK_4:
            	 scrollbarY = 0;
                 showVoterSummary = true;
                 initConsole();
                 repaint();
                 return true;
           default:
            	return false; // don't advance screen if no vote
            }
    	}
    	else {
    		keyPressed(e);
    		return true;
    	}
    }
	@Override public List<ConsoleOptions> getOptions() {
		GalacticCouncil c = galaxy().council();
		List<ConsoleOptions> options = new ArrayList<>();
        if (showVoterSummary) {
        	Empire pl = player();
        	String buttonText;
        	// Option 1
        	if (c.votingInProgress())
        		buttonText = text("COUNCIL_SHOW_VOTING");
        	else
        		buttonText = text("COUNCIL_SHOW_RESULTS");
			options.add(new ConsoleOptions(KeyEvent.VK_1, "1", buttonText));
        	// Option 2
        	if  (c.votingInProgress()) {
                if (c.hasVoted(pl) || options().isAutoPlay())
                	buttonText = text("COUNCIL_COMPLETE_VOTING");
                else {
                    buttonText = text("COUNCIL_SKIP_TO_PLAYER");
                    buttonText = pl.replaceTokens(buttonText, "player");
                }
                options.add(new ConsoleOptions(KeyEvent.VK_2, "2", buttonText));
            }
        }
        else
	    	switch (displayMode) {
				case ANNOUNCE:
					options.add(new ConsoleOptions(KeyEvent.VK_1, "1", text("COUNCIL_VIEW_SUMMARY")));
					options.add(new ConsoleOptions(KeyEvent.VK_2, "2", text("COUNCIL_BEGIN_VOTING")));
					break;
				case ACCEPT_RULING:
					options.add(new ConsoleOptions(KeyEvent.VK_1, "1", text("COUNCIL_ACCEPT_RULING")));
					if (!options().realmsBeyondCouncil())
						options.add(new ConsoleOptions(KeyEvent.VK_2, "2", text("COUNCIL_REJECT_RULING")));
					break;
				case ASK_PLAYER_VOTE:
					options.add(new ConsoleOptions(KeyEvent.VK_1, "1", "Vote " + c.candidate1().name()));
					options.add(new ConsoleOptions(KeyEvent.VK_2, "2", "Vote " + c.candidate2().name()));
					options.add(new ConsoleOptions(KeyEvent.VK_3, "3", text("COUNCIL_CHOICE_ABSTAIN")));
					options.add(new ConsoleOptions(KeyEvent.VK_4, "4", text("COUNCIL_VIEW_SUMMARY")));
					break;
				case NO_WINNER:
					options.add(new ConsoleOptions(KeyEvent.VK_ESCAPE, "C", text("COUNCIL_CONTINUE")));
					break;
				case SHOW_VOTE_RESULT:
					options.add(new ConsoleOptions(KeyEvent.VK_1, "1", text("COUNCIL_CONTINUE")));
		        	if  (c.votingInProgress()) {
		        		String buttonText;
		                if (c.hasVoted(player()) || options().isAutoPlay())
		                	buttonText = text("COUNCIL_COMPLETE_VOTING");
		                else {
		                    buttonText = text("COUNCIL_SKIP_TO_PLAYER");
		                    buttonText = player().replaceTokens(buttonText, "player");
		                }
		                options.add(new ConsoleOptions(KeyEvent.VK_2, "2", buttonText));
		            }
					options.add(new ConsoleOptions(KeyEvent.VK_4, "4", text("COUNCIL_VIEW_SUMMARY")));
					break;
				default:
					options.add(new ConsoleOptions(KeyEvent.VK_ESCAPE, "C", text("COUNCIL_CONTINUE")));
					break;
	        }
		return options;
	}
	@Override public String getMessage()	{
    	String msg = "";
        if (showVoterSummary)
        	msg += voterSummary();
        else
	    	switch (displayMode) {
				case ACCEPT_RULING:
					msg += acceptRulingMessage();
					break;
				case ANNOUNCE:
					msg += announceMessage();
					break;
				case ASK_PLAYER_VOTE:
					msg += askPlayerVoteMessage();
					break;
				case NO_WINNER:
					msg += noWinnerMessage();
					break;
				case SHOW_VOTE_RESULT:
					msg += showVoteResultMessage();
					break;
				default:
					break;
	        }
        msg += NEWLINE + NEWLINE + getMessageOption();
		return msg;
	}
	private String acceptRulingMessage()	{
        GalacticCouncil c = galaxy().council();
		String msg = text("COUNCIL_ELECTED_TITLE");
        msg += NEWLINE + voteTotals();
        msg += NEWLINE + "The winner is: " + c.leader().empireRaceName();
		return msg;
	}
	private String announceMessage()		{
        GalacticCouncil c = galaxy().council();
        Empire emp1 = c.candidate1();
        Empire emp2 = c.candidate2();
        String text1 = text("COUNCIL_CONVENE");
        String text2 = getModedText("COUNCIL_CONVENE2");
        text2 = emp1.replaceTokens(text2, "first");
        text2 = emp2.replaceTokens(text2, "second");

        String msg = text("COUNCIL_CONVENE_TITLE");
        msg += NEWLINE + text1;
        msg += NEWLINE + text2;
		return msg;
	}
	private String askPlayerVoteMessage() {
        GalacticCouncil c = galaxy().council();
        String msg = NEWLINE + voteTotals();
		msg += NEWLINE + NEWLINE + text("COUNCIL_CAST_PROMPT", str(c.nextVotes()));
        msg += NEWLINE + text("COUNCIL_CAST_PROMPT_TITLE");
		return msg;
	}
	private String noWinnerMessage() {
		String msg = NEWLINE + text("COUNCIL_ADJOURN_TITLE");
		msg += NEWLINE + voteTotals();
		msg += NEWLINE + getModedText("COUNCIL_ADJOURN");
		return msg;
	}
	private String showVoteResultMessage() {
        GalacticCouncil c = galaxy().council();
        String msg = NEWLINE;
        msg += NEWLINE + NEWLINE + voteTotals();
        if (c.lastVoted() == null) {
            String voteStr = text("COUNCIL_CAST_ABSTAIN", c.lastVotes());
            msg += c.lastVoter().replaceTokens(voteStr, "voter");
        }
        else {
        	String voteStr = text("COUNCIL_CAST_VOTE", str(c.lastVotes()));
        	voteStr = c.lastVoter().replaceTokens(voteStr, "voter");
            msg += c.lastVoted().replaceTokens(voteStr, "candidate");
        }
		return msg;
	}
    private String voteTotals() {
    	String msg = "";
        GalacticCouncil c = galaxy().council();
        msg += text("COUNCIL_VOTE_COUNT", str(c.votes1()));
        msg += " for " + c.candidate1().name();
        msg += " and " + text("COUNCIL_VOTE_COUNT", str(c.votes2()));
        msg += " for " + c.candidate2().name();
        return msg;
    }
    private String voterSummary() {
        GalacticCouncil c = galaxy().council();
        String msg = "";
		msg += NEWLINE + voteTotals();
        msg += NEWLINE + NEWLINE + text("COUNCIL_SUMMARY", str(c.votesToElect()));
        msg += NEWLINE;
        List<Empire> voters = c.voters();
		for (Empire voter : voters) {
			msg += NEWLINE + voterSummary(voter);
		}
        return msg;
    }
    private String voterSummary(Empire e)	{
        GalacticCouncil c = galaxy().council();
        boolean voted = c.hasVoted(e);
        int votes = c.votes(e);
        String voteStr;
        if (voted) {
            Empire votee = galaxy().empire(e.lastCouncilVoteEmpId());
            if (votee == null)
                voteStr = text("COUNCIL_VOTE_ABSTAINED", str(votes));
            else {
                voteStr = text("COUNCIL_VOTE_CAST", str(votes));
                voteStr = votee.replaceTokens(voteStr, "alien");
            }
        }
        else
            voteStr = text("COUNCIL_VOTE_COUNT", str(votes));
        String cand1Str = treatyString(e, c.candidate1());
        String cand2Str = treatyString(e, c.candidate2());
        
        String msg = e.raceName();
        msg += SPACER + voteStr;
        msg += SPACER + cand1Str;
        msg += SPACER + cand2Str;
        return msg;
    }
    private void initConsole()	{
    	if (!RotPUI.isVIPConsole)
    		return;
    	String year = displayYearOrTurn() + SPACER;
        if (showVoterSummary)
        	initConsoleSelection(year + "Voter Summary", true);
        else
	    	switch (displayMode) {
				case ACCEPT_RULING:
					initConsoleSelection(year + "Voter Summary", true);
					break;
				case ANNOUNCE:
					initConsoleSelection(year + "Council Opening", true);
					break;
				case ASK_PLAYER_VOTE:
					initConsoleSelection(year + "Player Vote", true);
					break;
				case NO_WINNER:
					initConsoleSelection(year + "No Winner", true);
					break;
				case SHOW_VOTE_RESULT:
					initConsoleSelection(year + "Vote Result", true);
					break;
				default:
					break;
	        }
    }
}
