package com.example.test_service.Modifiers;

public class WaterPresent extends Modifier {
    //private String input;
    private boolean isWater;

    public WaterPresent(boolean isWater) {
        this.isWater = isWater;
    }
    
    public WaterPresent(){
        isWater = true;
    }

    /*public void input(){
        Scanner s = new Scanner(System.in);
        while (isWater != true && isWater != false) {
            System.out.println("Is there water in the map?");
            input = s.nextLine();
            if (input.contains("no") || input.contains("No")) {
                isWater = false;
            } else if (input.contains("yes") || input.contains("Yes")) {
                isWater = true;
            } else {
                System.out.println("Invalid input. Please input yes or no:");
            }
        }
    } */


    public int execute(int finalNum){
        double finalVal = (double) finalNum;
        if (!isWater){
            if (finalVal < 130.0){
                finalVal = finalVal / 100.0 + 130.0;
            }

        }
        return (int) finalVal;
    }


}
