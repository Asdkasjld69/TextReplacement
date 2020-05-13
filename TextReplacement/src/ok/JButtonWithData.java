package ok;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;

public class JButtonWithData extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3618587312341785897L;
	private boolean toggle = true;
	private long lastpress = System.currentTimeMillis();
	private long timespressed = 0;
	private Map<Object,Color> colors;
	private JComponent[] components;
	public boolean getToggle() {
		return toggle;
	}
	public void setToggle(boolean toggle) {
		this.toggle = toggle;
	}
	public long getLastpress() {
		return lastpress;
	}
	public void setLastpress(long lastpress) {
		this.lastpress = lastpress;
	}
	public JButtonWithData() {
		super();
		colors = new HashMap<Object,Color>();
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				toggle = !toggle;
				setTimespressed(getTimespressed() + 1);
				lastpress = System.currentTimeMillis();
				setBackground(colors.get(toggle));
				toggleComponents();
			}
			
		});
		colors.put(true, new Color(32,224,64));
		colors.put(false, new Color(196,196,196));
	}
	public JButtonWithData(String text) {
		super(text);
		colors = new HashMap<Object,Color>();
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				toggle = !toggle;
				setTimespressed(getTimespressed() + 1);
				lastpress = System.currentTimeMillis();
				setBackground(colors.get(toggle));
				toggleComponents();
			}
			
		});
		colors.put(true, new Color(32,224,64));
		colors.put(false, new Color(196,196,196));
		setBackground(colors.get(toggle));
	}
	public long getTimespressed() {
		return timespressed;
	}
	public void setTimespressed(long timespressed) {
		this.timespressed = timespressed;
	}
	public Map<Object,Color> getColors() {
		return colors;
	}
	public void setColor(Object key,Color color) {
		colors.put(key, color);
		this.setBackground(colors.get(toggle));
	}
	public void toggleComponents() {
		if(components!=null) {
			for(JComponent c:components) {
				c.setVisible(toggle);
			}
		}
	}
	public JComponent[] getComponents() {
		return components;
	}
	public void setComponents(JComponent[] components) {
		this.components = components;
	}
	
}
