package top.jiehuan.kilikili.util;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public class DebugCanvas extends Canvas {
    private Vector logs;
    private int lineHeight = 15;
    private int scrollOffset = 0;

    public DebugCanvas() {
        logs = new Vector();
    }

    public void log(String msg) {
        if (logs.size() >= 200) {
            logs.removeElementAt(0); // 控制内存使用
        }
        logs.addElement(msg);

        // 自动滚动到底部
        scrollOffset = Math.max(0, logs.size() - getHeight() / lineHeight);
        repaint();
    }

    protected void paint(Graphics g) {
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(0, 0, 0);
        int maxLines = getHeight() / lineHeight;

        int start = scrollOffset;
        int end = Math.min(start + maxLines, logs.size());

        int y = 0;
        for (int i = start; i < end; i++) {
            String line = (String) logs.elementAt(i);
            g.drawString(line, 2, y, Graphics.TOP | Graphics.LEFT);
            y += lineHeight;
        }

        // 显示行数信息
        g.setColor(100, 100, 100);
        g.drawString("行 " + (start + 1) + "/" + logs.size(), getWidth() - 2, getHeight() - 2, Graphics.BOTTOM | Graphics.RIGHT);
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        int maxScroll = Math.max(0, logs.size() - getHeight() / lineHeight);

        if (action == UP) {
            scrollOffset = Math.max(0, scrollOffset - 1);
        } else if (action == DOWN) {
            scrollOffset = Math.min(maxScroll, scrollOffset + 1);
        }
        repaint();
    }
}
