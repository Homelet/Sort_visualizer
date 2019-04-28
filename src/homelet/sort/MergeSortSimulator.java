package homelet.sort;

import homelet.GH.handlers.GH;
import homelet.GH.utils.IntervalCaller;
import homelet.GH.visual.interfaces.Renderable;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * To sort an array of data, first divide them to half, continue until data is divided into sorted part(if a part has
 * only one data, then it is sorted), next merge all sorted parts into bigger sorted parts, until into one array
 */
public class MergeSortSimulator implements Renderable{
	
	public  Double[] data;
	IntervalCaller caller;
	int index, left_pointer, right_pointer;
	LinkedList<int[]> boundsList = new LinkedList<>();
	int               currentLeftBounds, currentMiddle, currentRightBounds;
	boolean  finished = false;
	Double[] subData;
	boolean  first    = true;
	private double   maxBounds, minbounds;
	private Color leftColor   = new Color(0xFF6600);
	private Color rightColor  = new Color(0x3300ff);
	private Color middleColor = new Color(0x33FF00);
	private Color otherColor  = new Color(0x999999);
	
	public MergeSortSimulator(Double[] doubles, double minBounds, double maxBounds){
		this.data = doubles;
		this.minbounds = minBounds;
		this.maxBounds = maxBounds;
		sort(data, 0, data.length - 1);
		this.caller = new IntervalCaller(this::doingRevolution, 0L);
	}
	
	private void sort(Double[] data, int leftBounds, int rightBounds){
		if(leftBounds >= rightBounds)
			return;
		int middle = (leftBounds + rightBounds) / 2;
		sort(data, leftBounds, middle);
		sort(data, middle + 1, rightBounds);
		boundsList.add(new int[]{ leftBounds, middle, rightBounds });
	}
	
	private void preparingRevolution(){
		int[] current = boundsList.poll();
		if(current == null){
			finished = true;
			return;
		}
		this.currentLeftBounds = current[0];
		this.currentRightBounds = current[2];
		this.currentMiddle = current[1];
		this.subData = Arrays.copyOfRange(data, currentLeftBounds, currentRightBounds + 1);
		this.left_pointer = currentLeftBounds;
		this.right_pointer = currentMiddle + 1;
		this.index = currentLeftBounds;
		doingRevolution();
	}
	
	private void doingRevolution(){
		if(first || index > currentRightBounds){
			first = false;
			finishingRevolution();
			return;
		}
//		System.out.println(index + ", " + left_pointer + ", " + right_pointer + "|, " + currentLeftBounds + ", " + currentMiddle + ", " + currentRightBounds + ", " + subData.length);
		if(left_pointer > currentMiddle){                                                                           // if all left has been processed
			data[index] = subData[right_pointer - currentLeftBounds];
			right_pointer++;
		}else if(right_pointer > currentRightBounds){                                                            // if all right has been processed
			data[index] = subData[left_pointer - currentLeftBounds];
			left_pointer++;
		}else if(subData[left_pointer - currentLeftBounds].compareTo(subData[right_pointer - currentLeftBounds]) < 0){  // if left data < right data
			data[index] = subData[left_pointer - currentLeftBounds];
			left_pointer++;
		}else{                                                                                            // if left data >= right data
			data[index] = subData[right_pointer - currentLeftBounds];
			right_pointer++;
		}
		this.index++;
	}

	private void finishingRevolution(){
		preparingRevolution();
	}

	@Override
	public void tick(){
		caller.tick();
	}

	@Override
	public void render(Graphics2D g){
		Rectangle bounds     = g.getClipBounds();
		double    unitHeight = bounds.getHeight() / (maxBounds - minbounds);
		double    unitWidth  = bounds.getWidth() / data.length;
		for(int index = 0; index < data.length; index++){
			double value = data[index];
			Shape  shape = GH.rectangle(false, index * unitWidth, (maxBounds - value) * unitHeight, unitWidth, value * unitHeight);
			if(!finished){
				if(index >= currentLeftBounds && index < currentMiddle){
					g.setColor(leftColor);
				}else if(index == currentMiddle){
					g.setColor(middleColor);
				}else if(index > currentMiddle && index <= currentRightBounds){
					g.setColor(rightColor);
				}else{
					g.setColor(otherColor);
				}
			}else{
				g.setColor(leftColor);
			}
			g.fill(shape);
			g.setColor(Color.BLACK);
			g.draw(shape);
		}
	}
	
	@Override
	public boolean isTicking(){
		return !finished;
	}
}
