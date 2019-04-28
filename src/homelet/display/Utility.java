package homelet.display;

public class Utility{
	
	public static Double[] getData(int amount, double min, double max){
		Double[] value = new Double[amount];
		for(int index = 0; index < amount; index++){
			value[index] = getRandomNumber(min, max);
		}
		return value;
	}
	
	public static double getRandomNumber(double min, double max){
		return (Math.random() * (max - min)) + min;
	}
	
	public static <E extends Comparable<E>> boolean checkSorted(E[] data){
		for(int index = 1; index < data.length; index++){
			if(data[index].compareTo(data[index - 1]) < 0){
				return false;
			}
		}
		return true;
	}
}
