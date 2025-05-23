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
package rotp.ui.tech;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import rotp.model.empires.DiplomaticTreaty;
import rotp.model.empires.Empire;
import rotp.model.empires.EmpireView;
import rotp.ui.FadeInPanel;
import rotp.ui.RotPUI;
import rotp.ui.diplomacy.DialogueManager;
import rotp.ui.diplomacy.DiplomacyRequestReply;
import rotp.ui.diplomacy.DiplomaticMessage;
import rotp.ui.main.SystemPanel;
import rotp.ui.notifications.DiplomaticNotification;
import rotp.ui.vipconsole.IVIPListener;

public class DiplomaticMessageUI extends FadeInPanel 
		implements MouseListener, MouseMotionListener, ActionListener, IVIPListener {
    private static final long serialVersionUID = 1L;
    // static Color innerTextBackC = new Color(73,163,163);
    // static Color outerTextAreaC = new Color(92,208,208);
    // private static Color textBorderLo1 = new Color(73,163,163);
    // private static Color textBorderLo2 = new Color(52,126,126);
    // private static Color textBorderHi1  = new Color(110,240,240);
    // private static Color textBorderHi2  = new Color(115,252,252);
    private static Color textC = Color.white;
    private static Color textBgC = Color.darkGray;
    private static Color optionC = Color.white;
    private static Color hoverOptionC = Color.yellow;
    private static Color disabledOptionC = Color.gray;

    // static Border outerTextAreaBorder, innerTextAreaBorder;

    private Image flagPole;

    private final Rectangle[] selectBoxes = new Rectangle[DiplomaticMessage.MAX_SELECTIONS];
    private int selectHover = -1;

    private Empire diplomatEmpire;
    private Image flag, dialogBox;
    private DiplomaticMessage message;
    private String messageRemark, messageRemarkDetail;

    private int talkTimeMs = 5000;
    private long startTimeMs;
    private float holoPct = 0f;
    private boolean hasSpoken = false;
    private boolean mouseSet = false;
    private boolean exited = false;

    public DiplomaticMessageUI() {
        // outerTextAreaBorder = new ThickBevelBorder(8, textBorderHi2, textBorderHi1, textBorderHi2, textBorderHi1, textBorderLo2, textBorderLo1, textBorderLo2, textBorderLo1);
        // innerTextAreaBorder = new ThickBevelBorder(8, textBorderLo1, textBorderLo2, textBorderLo1, textBorderLo2, textBorderHi1, textBorderHi2, textBorderHi1, textBorderHi2);

        for (int i=0;i<selectBoxes.length;i++)
            selectBoxes[i] = new Rectangle();
        initModel();
    }
    private void initModel() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    @Override
    public String ambienceSoundKey() { return diplomatEmpire.isPlayer() ? defaultAmbience() : diplomatEmpire.diplomacyTheme(); }

    public boolean init(DiplomaticNotification notif) {
        clearBuffer();
        diplomatEmpire = notif.talker();
        if (diplomatEmpire.isPlayer()) {
            flag = player().flagNorm();
            dialogBox = player().dialogNorm();
        }
        else {
        	EmpireView view = player().viewForEmpire(diplomatEmpire);
        	if (view.embassy().muted() &&
        			( notif.type().equals(DialogueManager.WARNING_ESPIONAGE)
        			|| notif.type().equals(DialogueManager.WARNING_SABOTAGE))) {
        		log("Skipped Espionage / sabotage Warning");
        		// System.out.println("Skipped Espionage / sabotage Warning");
                exited = true;
            	return false;
        	}
            flag = view.flag();
            dialogBox = view.dialogueBox();
        }

        diplomatEmpire.resetDiplomat();
        message = DialogueManager.current().message(notif.type(), notif.incident(), diplomatEmpire, notif.otherEmpire());
        message.returnToMap(notif.returnToMap());
        messageRemark = "";
        if (message == null)
            messageRemark = concat("Message type not defined: ", notif.type());
        else if (notif.otherEmpire() == null)
            messageRemark = diplomatEmpire.decode(message.remark(notif.otherEmpire()), player());
        else 
            messageRemark = diplomatEmpire.decode(message.remark(notif.otherEmpire()), player());

        commonInit();
        initForConsole();

        if (!diplomatEmpire.isPlayer()
        		&& notif.type() == DialogueManager.OFFER_TRADE
        		&& player().atWarWith(diplomatEmpire.id)) {
        	log("Skipped Offer Trade by Empire now at war");
        	// System.out.println("Skipped Offer Trade by Empire now at war");
            exited = true;
            message.escape();
        	return false;
        }
        
        return true;
    }
    public void initReply(DiplomacyRequestReply reply) {
        diplomatEmpire = reply.view().owner();
        if (diplomatEmpire.isPlayer()) {
            flag = player().flagNorm();
            dialogBox = player().dialogNorm();
        }
        else {
            flag = player().viewForEmpire(diplomatEmpire).flag();
            dialogBox = player().viewForEmpire(diplomatEmpire).dialogueBox();
        }
        diplomatEmpire.resetDiplomat();
        message = reply;
        messageRemark = reply.remark();
        commonInit();
        initReplyForConsole();
    }
    private void commonInit() {
        exited = false;
        if (message == null) {
            err(messageRemark);
            return;
        }

        messageRemarkDetail = message.requestDetail();
        if (message.showTalking() && playAnimations())
            startFadeTimer();
        else
            endFade();
        selectHover = -1;
        mouseSet = false;
        hasSpoken = false;
        talkTimeMs = playAnimations() ? min(500, messageRemark.length() * 30) : 0;
        startTimeMs = playAnimations() ? System.currentTimeMillis() : 0;
        if (flagPole == null)
            flagPole = currentFrame("FlagPole");
    }
    private void clearSelections() {
        for (Rectangle r: selectBoxes)
            r.setBounds(0,0,0,0);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image img0 = paintToImage();
        g.drawImage(img0,0,0,null);
        drawOverlay(g);
    }
    private Image paintToImage() {
        if (message == null) 
            err(messageRemark);
        
        clearSelections();
        boolean talking = message.showTalking() && ((System.currentTimeMillis() - startTimeMs) < talkTimeMs);
        boolean receiving = message.showTalking() && ((System.currentTimeMillis() - startTimeMs) < 200);

		BufferedImage labImg = diplomatEmpire.embassy();
		BufferedImage holoImg = diplomatEmpire.holograph();
		Image raceImg = talking ? diplomatEmpire.diplomatTalking() : diplomatEmpire.diplomatQuiet();

        int w = getWidth();
        int h = getHeight();

        int w0 = labImg.getWidth();
        int h0 = labImg.getHeight();
        //  location of flag
		int fW = scaled(diplomatEmpire.flagW());
		int fH = scaled(diplomatEmpire.flagH());
		int fX = (int)(w-(diplomatEmpire.labFlagX()*w))-(fW/2);
        int fY = (h*4/10)-(fH/2);

        Image dataImg = screenBuffer();
        Graphics2D g = (Graphics2D) dataImg.getGraphics();
        super.paintComponent(g);
        g.drawImage(labImg, w, 0, 0, h, 0, 0, labImg.getWidth(), labImg.getHeight(), null);

        Color c0 = new Color(0,0,0,128);
        Color c1 = new Color(0,0,0,0);
        float dist[] = new float[] { 0.0f, 1.0f };
        Color colors[] = new Color[] { c0, c1 };
        int topPad = s100;
        int hPad = s100;
        int boxH = fH+s100+s100;
        int boxW = fW-s60;
        int boxX = fX+s30;
        int boxY = fY;
        g.setColor(c0);
        g.fillRect(boxX, boxY, boxW, boxH);
        // top gradient
        g.setPaint(new GradientPaint(new Point2D.Float(boxX,boxY-topPad), c1, new Point2D.Float(boxX,boxY),c0));
        g.fillRect(boxX, boxY-topPad, boxW, topPad);
        // left gradient
        g.setPaint(new GradientPaint(new Point2D.Float(boxX-hPad,boxY), c1, new Point2D.Float(boxX,boxY),c0));
        g.fillRect(boxX-hPad, boxY, hPad, boxH);
        // right gradient
        g.setPaint(new GradientPaint(new Point2D.Float(boxX+boxW,boxY), c0, new Point2D.Float(boxX+boxW+hPad,boxY),c1));
        g.fillRect(boxX+boxW, boxY, hPad, boxH);
        // top-left gradient
        g.setPaint(new RadialGradientPaint(new Rectangle2D.Float(boxX-hPad,boxY-topPad,hPad+topPad,hPad+topPad), dist, colors, CycleMethod.NO_CYCLE));
        g.setClip(boxX-hPad, boxY-topPad, hPad, topPad);
        g.fillRect(boxX-hPad, boxY-topPad, hPad,topPad);
        // top-right gradient
        g.setPaint(new RadialGradientPaint(new Rectangle2D.Float(boxX+boxW-hPad,boxY-topPad,hPad+topPad,hPad+topPad), dist, colors, CycleMethod.NO_CYCLE));
        g.setClip(boxX+boxW, boxY-topPad, hPad, topPad);
        g.fillRect(boxX+boxW, boxY-topPad, hPad,topPad);
        g.setClip(null);
        // draw oscillating holograph
        if (holoImg != null) {
            Composite prevComposite = g.getComposite();
            float fluxPct = (holoPct % 1) / 2;
            fluxPct = (fluxPct <= 0.25) ?  1 - fluxPct : 0.5f + fluxPct;
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fluxPct);
            g.setComposite(ac);
            g.drawImage(holoImg, w, 0, 0, h, 0, 0, w0, h0, null);
            g.setComposite(prevComposite);
        }

        // draw flag
        g.drawImage(flag, fX, fY, fW, fH, null);
        
        
        // draw empire info
        if (!diplomatEmpire.isPlayer()) {
            int empY = scaled(420);
            g.setFont(narrowFont(24));
            g.setColor(SystemPanel.whiteText);
            String s = diplomatEmpire.name();
            int sw = g.getFontMetrics().stringWidth(s);
            drawBorderedString(g, s, fX+(fW-sw)/2, empY, Color.black, Color.white);
            //drawString(g,s, fX+(fW-sw)/2, empY);
            empY += s20;
            g.setFont(narrowFont(18));
            s = text("LEADER_PERSONALITY_FORMAT", diplomatEmpire.leader().personality(), diplomatEmpire.leader().objective());
            sw = g.getFontMetrics().stringWidth(s); 
            drawBorderedString(g, s, fX+(fW-sw)/2, empY, Color.black, Color.white);
//            drawString(g,s, fX+(fW-sw)/2, empY);
            empY += s20;
            DiplomaticTreaty treaty = player().treatyWithEmpire(diplomatEmpire.id);
            s = treaty.status(player());
            if (treaty.isPeace() && options().isColdWarMode())
            	s += " " + text("RACES_COLD_WAR");
            sw = g.getFontMetrics().stringWidth(s);
            drawBorderedString(g, s, fX+(fW-sw)/2, empY, Color.black, Color.white);
//            drawString(g,s, fX+(fW-sw)/2, empY);
        }

        // draw diplomat
        g.drawImage(raceImg, w, 0, 0, h, 0, 0, raceImg.getWidth(null), raceImg.getHeight(null), null);

        // draw game image to screen, flipped horizontally
        //g.drawImage(dataImg, w, 0, 0, h, 0, 0, w, h, null);

        // draw dialog image to screen
        int dlgW = dialogBox.getWidth(null);
        int dlgH = dialogBox.getHeight(null);
        int dlgH2 = scaled(dlgH);
        int dlgW2 = scaled(dlgW);
        int dlgY = h-dlgH2;
        g.drawImage(dialogBox, 0, dlgY, dlgW2, dlgH2, null);

        int rMargin = scaled(player().dialogRightMargin());

        int textBoxX = scaled(player().dialogLeftMargin());
        int textBoxY = scaled(player().dialogTopY());
        int textBoxW = w-textBoxX-rMargin;
        int textBoxH = h-textBoxY;

        drawText(g, receiving, textBoxX, textBoxY, textBoxW, textBoxH);

        g.dispose();
        return screenBuffer();
    }
    private void drawText(Graphics g, boolean receiving, int x, int y, int w, int h) {
        if (message == null) {
            err(messageRemark);
            return;
        }

        // messageRemark is the prose text that the player reads
        // messageRemarkDetail is infrequent, but is a differently-colored suffix to the messageRemark
        //    such as [your were framed]
        // message.numReplies() is the number of response options provided to the player
        // message.numDataLines() similar to response options but 2/line
        
        g.setColor(textC);

        int x1 = x+s20;
        int y1 = y-s5;
        int w1 = w-s20-s20;

        boolean showRemarkOnly = false;
        // draw remark
        String displayText = messageRemarkDetail.isEmpty() ? messageRemark : messageRemark+" "+messageRemarkDetail;
        if (!diplomatEmpire.isPlayer() && message.showTalking()) {
            showRemarkOnly = true;
            if (receiving)
                displayText = text("DIPLOMACY_RECEIVING");
            else
                showRemarkOnly = false;
        }

        // draw the remark...  may be multi-line
        // data lines will draw two items per line
        int nonRemarkLines = message.numReplies()+(message.numDataLines()+1)/2;
        int maxLinesForText = nonRemarkLines > 3 ? 2 : 6-nonRemarkLines;
        // need to calculate correct font size to fit full text (remark & detail) onto allowed # of lines
        int dispFontSize = scaledDialogueOrPlainFontSize(g, displayText, w1, maxLinesForText, 26, 16);
        if (dispFontSize >0) {
        	g.setFont(dlgFont(dispFontSize));
        }
        else {
        	dispFontSize = -dispFontSize;
        	g.setFont(plainFont(dispFontSize));
        }
        // now split just the remark text 
        List<String> remarkLines = wrappedLines(g, messageRemark, w1);
        int lineH = scaled(dispFontSize-2);
        int lastLineW = 0;
        // print the remark text
        for (String line: remarkLines) {
            y1 += lineH;
            drawBorderedString(g, line, 1, x1, y1, textBgC,  textC);
            lastLineW = g.getFontMetrics().stringWidth(line);
        }
        
        if (!messageRemarkDetail.isEmpty()) {
            g.setColor(Color.lightGray);
            int fontH = unscaled(g.getFontMetrics().getHeight());
            g.setFont(narrowFont(max(12,fontH-4)));
            int detailW = g.getFontMetrics().stringWidth(messageRemarkDetail);
            int x1a = x1+lastLineW+s10;
            if ((detailW+x1a) > w1) {
                x1a = x1;
                y1 += lineH;
            }
            drawString(g,messageRemarkDetail, x1a, y1);
        }

        y1 += s10;
        if (showRemarkOnly)
            return;

        // don't show options until ready to click
        if (waitingOnMessage())
            return;

        // if this is a simple message  with no replies, display as wrapped text
        // and enable selection on any portion of the screen
        if (message.numReplies() == 0) {
            selectBoxes[0].setBounds(0,0,getWidth(),getHeight());
            return;
        }

        // space remaining (in actual px)
        int ySpace = h - (y1-y)-s35;

        // calculate line height size for options (max 36)
        int optionLineH = min(s24, ySpace / nonRemarkLines);
        lineH = optionLineH;
        int yGap = (ySpace-(lineH*nonRemarkLines))/2;

        // calculate option font size based on line height
        int fontSize = unscaled(optionLineH) - 2;

        if (fontSize < 10) {
            //err("Font too small. size:", str(fontSize), "  for messageText:  ", displayText);
            fontSize = 10;
        }

        // draw data lines, smaller text & indented
        int dataLines = message.numDataLines();
        if (dataLines > 0) {
            int margin1 = s40;
            int margin2 = w1/2+s20;
            int x2 = x1;
            for (int i=0;i<dataLines;i++) {
                if (i % 2 == 0) {
                    y1 += (lineH+s2);
                    x2 = x1+margin1;
                }
                else 
                    x2 = x1+margin2;
                String reply = message.dataLine(i);
                g.fillOval(x1, y1-s9, s3, s3);
                drawBorderedString(g, reply, 1, x2+s10, y1, textBgC, textC);
            }
        }
        // draw options, smaller text & indented
        y1 += yGap;
        x1 += s20;
        Color c0, c0b;
        Color c1 = new Color(255,255,0,64);
        for (int i=0;i<message.numReplies();i++) {
            c0b = disabledOptionC;
            g.setFont(narrowFont(fontSize));
            y1 += (lineH+s2);
            if (!message.enabled(i))
                c0 = disabledOptionC;
            else if (i == selectHover) {
                c0 = hoverOptionC;
                c0b = hoverOptionC;
                g.setColor(c1);
                g.fillRoundRect(x,y1-lineH+s5, w, lineH, lineH, lineH);
            }
            else 
                c0 = optionC;
            g.setColor(c0);
            drawString(g,""+(i+1), x1-s15, y1);
            g.fillOval(x1, y1-s9, s3, s3);
            String reply = message.reply(i);
            String replyDetail = message.replyDetail(i);
            int sw1 = g.getFontMetrics().stringWidth(reply);
            drawBorderedString(g, reply, 1, x1+s10, y1, textBgC, c0);
            if (!replyDetail.isEmpty()) {
                g.setFont(narrowFont(fontSize-4));
                drawBorderedString(g, replyDetail, 1, x1+s30+sw1, y1, textBgC, c0b);
            }
            selectBoxes[i].setBounds(x1-s15, y1-lineH+s5, w, lineH);
        }
        findAndSetMouse();
    }
    private void selectOption(int opt) {
        if (selectHover < 0)
            return;
        if (stillFading() || waitingOnMessage())
            return;
        exited = true;
        softClick();
        message.select(opt);
    }
    private boolean waitingOnMessage() {
        if (diplomatEmpire.isPlayer() || !message.showTalking())
            return false;
        return (System.currentTimeMillis() - startTimeMs) < 500;
    }
    private void setMouseLocation(int x, int y) {
        int prevHover = selectHover;
        selectHover = -1;
        for (int i=0;i<selectBoxes.length;i++) {
            if (selectBoxes[i].contains(x,y))
                selectHover = i;
        }
        if (prevHover != selectHover)
            repaint();
    }
    private void findAndSetMouse() {
        if (!mouseSet) {
            mouseSet = true;
            Point p = MouseInfo.getPointerInfo().getLocation();
            try {
                Point p0 = getLocationOnScreen();
                setMouseLocation(p.x-p0.x, p.y-p0.y);
            }
            catch(Exception e) {
                // sometimes getLocationOnScreen() breaks if this panel is no longer active
            }
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (waitingOnMessage())
                return;

        int k = e.getKeyCode();

        switch(k) {
            case KeyEvent.VK_1: selectHover = 0; selectOption(0); return;
            case KeyEvent.VK_2: selectHover = 1; selectOption(1); return;
            case KeyEvent.VK_3: selectHover = 2; selectOption(2); return;
            case KeyEvent.VK_4: selectHover = 3; selectOption(3); return;
            case KeyEvent.VK_5: selectHover = 4; selectOption(4); return;
            case KeyEvent.VK_6: selectHover = 5; selectOption(5); return;
            case KeyEvent.VK_T:
            	// BR: More research INFO
            	RotPUI.instance().selectTechPanel(1);
            	return;
            case KeyEvent.VK_ESCAPE:
                exited = true;
                message.escape();
        }
    }

    @Override
    public void mouseDragged(MouseEvent arg0) { }
    @Override
    public void mouseMoved(MouseEvent e) {
        setMouseLocation(e.getX(), e.getY());
    }
    @Override
    public void mouseClicked(MouseEvent arg0) { }
    @Override
    public void mouseEntered(MouseEvent arg0) { }
    @Override
    public void mouseExited(MouseEvent arg0) { }
    @Override
    public void mousePressed(MouseEvent arg0) { }
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() > 3)
            return;
        if (selectHover < 0)
            return;
        selectOption(selectHover);
    }

    @Override
    public void animate() {
        if (!playAnimations())
            return;

        advanceFade();
        holoPct += .1;
        if (!stillFading()) {
            if (!hasSpoken) {
                hasSpoken = true;
                talkTimeMs = 1000 + fadeInMs();
            }
        }
        repaint();
    }

    // ##### Console Tools
    private String getEmpireInfo(String sep)	{
    	DiplomaticTreaty treaty = player().treatyWithEmpire(diplomatEmpire.id);
    	String info = text("LEADER_PERSONALITY_FORMAT",
    						diplomatEmpire.leader().personality(),
    						diplomatEmpire.leader().objective());
    	info += sep + treaty.status(player());
        if (treaty.isPeace() && options().isColdWarMode())
        	info += " " + text("RACES_COLD_WAR");
    	return info;
    }
    public boolean[] consoleResponse(String s)	{
    	boolean validResponse = false;
    	switch (s.toUpperCase()) {
			case "0": case "ESC": case "ESCAPE":
				exited = true;
				validResponse = true;
	            message.escape();
				break;
			case "1": case "2": case "3": case "4": case "5": case "6":
				int response = (int)s.charAt(0) - (int)'1';
				if (response < message.numReplies()) {
					selectHover = response;
					selectOption(selectHover);
					validResponse = true;
				}
				else
					validResponse = false;
				break;
		}
    	return new boolean[] {exited, validResponse};
    }
    private void initForConsole()				{
    	if (!RotPUI.isVIPConsole)
    		return;
    	String title = "Dialogue with " + diplomatEmpire.name();
    	talkTimeMs = 10;
    	initConsoleSelection(title, true);
    }
    private void initReplyForConsole()			{
    	if (!RotPUI.isVIPConsole)
    		return;
    	String title = "Final reply from " + diplomatEmpire.name();
    	talkTimeMs = 10;
    	initConsoleSelection(title, true);
    }
    @Override public boolean handleKeyPress(KeyEvent e)	{
    	keyPressed(e);
    	repaint();
    	return true;
    }
	@Override public List<ConsoleOptions> getOptions()	{
		List<ConsoleOptions> options = new ArrayList<>();
		int numReplies = message.numReplies();
		//options.add(new ConsoleOptions(KeyEvent.VK_0, "0", "Continue"));
		if (numReplies > 0) {
			for (int i=0; i<numReplies; i++) {
				if (message.enabled(i) ) {
					int keyCode	= KeyEvent.VK_1 + i;
	        		String key	= Character.toString ((char) keyCode);
	        		String txt	= message.reply(i) + " " + message.replyDetail(i);
					options.add(new ConsoleOptions(keyCode, key, txt));
				}
			}
		}
		return options;
	}
	@Override public String getMessage() {
		String msg = getEmpireInfo(NEWLINE);
		msg += NEWLINE + "Translated Message = ";
		msg += NEWLINE + messageRemark;
		String remarkDetails = messageRemarkDetail;
		if (!remarkDetails.isEmpty())
			msg += NEWLINE + remarkDetails;
		msg += NEWLINE + getMessageOption();
		repaint();
		return msg;
	}
	@Override public boolean exited()	{ return exited; }
}
