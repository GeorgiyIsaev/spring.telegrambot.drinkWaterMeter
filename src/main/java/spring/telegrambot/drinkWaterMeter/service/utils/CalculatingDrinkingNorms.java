package spring.telegrambot.drinkWaterMeter.service.utils;

public class CalculatingDrinkingNorms {
    //В 2019 г. эксперты ВОЗ отметили, что нормы ежедневного потребления воды не существует.
    // В среднем в сутки необходимо выпивать от одного до пяти литров воды
    //Рассчитывается общий объем жидкости, куда входит вода, чай, кофе, компот, супы, соки (и фрукты).
    //Чистой питьевой воды же рекомендуется употреблять 50-70% от общего объема всей жидкости.
    //Детям воды нужно в два раза больше на кг массы
    //При занятии спортом рекомендуется пить на 500-600мл в день больше
    //Норма потребления воды зависит от веса и возраста человека, погодных условий и физических нагрузок

    private final static CalculatingDrinkingNorms calculatingDrinkingNorms = new CalculatingDrinkingNorms();
   public static CalculatingDrinkingNorms of(){
        return calculatingDrinkingNorms;
   }

   public int recommendedWeightM(int height){
        //формула Брокка для мужчин
       double weight = (height-100) * 1.05;
       return (int) weight;

   }
    public int recommendedWeightW(int height){
        //формула лоренца для женщин
        double weight =    (height - 100) - (double) (height - 150) / 2;
        return (int) weight;
    }

    public int drinkingNorms(int recommendedWeight){
        return recommendedWeight * 28; //25-30 мл на 1 кг рекомендуемой (идеальной) массы тела.
    }

    public int drinkingOverweight(int recommendedWeight){
        return recommendedWeight * 40; //При избыточном весе рекомендуется больше воды.
    }

    public int recommendedWeight(int height, Boolean isWoman){
        if(isWoman == null || isWoman){
            return recommendedWeightW(height);
        }
        else{
            return recommendedWeightM(height);
        }
    }


    public boolean isOverweight(int height, int weight, Boolean isWoman){
        int recommendedWeight = recommendedWeight(height, isWoman);
        return weight > recommendedWeight;
    }

    public int calculating(int height, int weight, Boolean isWoman){
        if(isOverweight(height,weight,isWoman)){
            return drinkingOverweight(recommendedWeight(height,isWoman));
        }
        else {
            return drinkingNorms(recommendedWeight(height,isWoman));
        }
    }

}
