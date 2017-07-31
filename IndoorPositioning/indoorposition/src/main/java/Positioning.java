import java.util.ArrayList;
import java.util.List;

import entity.AccessPiont;
import entity.FingerPrint;
import entity.OnlineFingerPrint;

/**
 * Created by King on 2017/7/30.
 */

public class Positioning {
    private int x;
    private int y;
    private List<AccessPiont> accessPionts;
    private List<OnlineFingerPrint> onlineFingerPrints;

    public Positioning(List<AccessPiont> accessPionts, List<OnlineFingerPrint> onlineFingerPrints) {
        this.accessPionts = accessPionts;
        this.onlineFingerPrints = onlineFingerPrints;
    }

    public  int nnAlgorithm(){
        int index = 0;
        ArrayList<Double> nnDistance = new ArrayList<Double>();
        for(int j = 0; j < accessPionts.get(0).getRssiList().size(); j++){
            nnDistance.add(j,0.0);
        }
        for(AccessPiont accessPiont : accessPionts){
            for(OnlineFingerPrint fingerPrint : onlineFingerPrints){
                if(fingerPrint.getMacAddress().equals(accessPiont.getMacAddress())){
                    for(int i = 0; i < accessPiont.getRssiList().size(); i++){
                        nnDistance.set(i,nnDistance.get(i)+Math.pow(accessPiont.getRssiList().get(i) - fingerPrint.getRssi(),2));
                    }
                }
            }
            Double minknnDistance =nnDistance.get(0);
            for(int i=1;i< nnDistance.size();i++){
                if(nnDistance.get(i)<minknnDistance){
                    index = i;
                    minknnDistance = nnDistance.get(i);
                }
            }
        }
        return index;
    }
}
