package com.kc.demo.vo;

import com.kc.demo.dao.PraiseTreadMapper;
import com.kc.demo.model.PraiseTread;

public class PraiseTreadVo {

    public static void Praise(PraiseTread praiseTread, PraiseTread praiseTreadIn, PraiseTreadMapper praiseTreadMapper,Integer praiseCount,Integer treadCount){
        if(praiseTread == null) {
            praiseTreadIn.setIspraise(true);
            praiseCount += 1;
            praiseTreadMapper.insert(praiseTreadIn);
        }else {
            if(praiseTread.getIspraise() == null) {
                praiseTreadIn.setIspraise(true);
                praiseCount += 1;
                if(praiseTread.getIstread() != null && praiseTread.getIstread() == true){
                    praiseTreadIn.setIstread(false);
                    treadCount -= 1;

                }
            } else{
                if(praiseTread.getIspraise() == true){
                    praiseTreadIn.setIspraise(false);
                    praiseCount -= 1;
                }else{
                    praiseTreadIn.setIspraise(true);
                    praiseCount += 1;
                    if(praiseTread.getIstread() != null && praiseTread.getIstread() == true){
                        praiseTreadIn.setIstread(false);
                        treadCount -= 1;
                    }
                }
            }
            praiseTreadMapper.updatePraiseTread(praiseTreadIn);
    }
    }

    public static void detailPraiseTread(PraiseTread praiseTread,ViewDetailVo detailVo){
        if(praiseTread!=null){
            if(praiseTread.getIspraise()==null){
                detailVo.setIspraise(false);
            }else {
                detailVo.setIspraise(praiseTread.getIspraise());
            }
            if(praiseTread.getIstread()==null){
                detailVo.setIstread(false);
            }else {
                detailVo.setIstread(praiseTread.getIstread());
            }
            if(praiseTread.getIscollection()==null){
                detailVo.setIscollection(false);
            }else {
                detailVo.setIscollection(praiseTread.getIscollection());
            }
        }else {
            detailVo.setIspraise(false);
            detailVo.setIstread(false);
            detailVo.setIscollection(false);
        }
    }

}
