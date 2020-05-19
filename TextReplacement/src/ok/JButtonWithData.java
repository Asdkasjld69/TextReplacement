package ok;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
	private Object[] cycle = new Object[] {true,false};
	private String[] cycle_text = new String[] {""};
	private int index=0;
	private int index_text=0;
	private long lastpress = System.currentTimeMillis();
	private long timespressed = 0;
	private Map<Object,Color> colors = new HashMap<Object,Color>();
	private JComponent[] targets = null;
	
	public String[] getCycle_text() {
		return cycle_text;
	}
	public void setCycle_text(String[] cycle_text) {
		this.cycle_text = cycle_text;
	}
	public Object[] getCycle() {
		return cycle;
	}
	public void setCycle(Object[] cycle) {
		this.cycle = cycle;
	}
	
	public Object getData() {
		return cycle[index];
	}
	
	public long getLastpress() {
		return lastpress;
	}
	public void setLastpress(long lastpress) {
		this.lastpress = lastpress;
	}
	public JButtonWithData() {
		super();
		colors.put(true, new Color(32,224,64));
		colors.put(false, new Color(196,196,196));
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				index++;
				index_text++;
				if(index>=cycle.length) {
					index = 0;
				}
				if(index_text>=cycle_text.length) {
					index_text = 0;
				}
				setTimespressed(getTimespressed() + 1);
				lastpress = System.currentTimeMillis();
				setBackground(colors.get(cycle[index]));
				setText(cycle_text[index_text]);
				toggleTargets();
			}
			
		});
		
		setBackground(colors.get(cycle[index]));
	}
	public JButtonWithData(String text) {
		super(text);
		cycle_text= new String[]{text};
		colors.put(true, new Color(32,224,64));
		colors.put(false, new Color(196,196,196));
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cycle();
			}
			
		});
		colors.put(true, new Color(32,224,64));
		colors.put(false, new Color(196,196,196));
		setBackground(colors.get(cycle[index]));
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
	public void setColors(Map<Object,Color> colors) {
		this.colors = colors;
	}
	public void setColor(Object key,Color color) {
		colors.put(key, color);
		this.setBackground(colors.get(cycle[index]));
	}
	public void toggleTargets() {
		if(targets!=null&&targets.length>0) {
			for(JComponent c:targets) {
				c.setVisible((index%2==0));
			}
		}
	}
	public JComponent[] getTargets() {
		return targets;
	}
	public void setTargets(JComponent[] targets) {
		this.targets = targets;
	}
	public void cycle() {
		index++;
		index_text++;
		if(index>=cycle.length) {
			index = 0;
		}
		if(index_text>=cycle_text.length) {
			index_text = 0;
		}
		setTimespressed(getTimespressed() + 1);
		lastpress = System.currentTimeMillis();
		setBackground(colors.get(cycle[index]));
		setText(cycle_text[index_text]);
		toggleTargets();
	}
	
}
