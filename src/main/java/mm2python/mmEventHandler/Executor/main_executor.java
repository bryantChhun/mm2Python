/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmEventHandler.Executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author bryant.chhun
 */

/**
 * Singleton executor for window and data creation threads
 */
public class main_executor {
    
    private static ExecutorService mmExecutor;
    
    public static ExecutorService getExecutor(){
        if(mmExecutor == null) {
            mmExecutor = Executors.newFixedThreadPool(20);
        }
        return mmExecutor;
    }
    
}
