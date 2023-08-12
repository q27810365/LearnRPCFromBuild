package org.example.loadbalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance{

    @Override
    public String balance(List<String> addressList) {

        Random random = new Random();
        int choose = random.nextInt(addressList.size());

        System.out.println(this.getClass().getName());
        return addressList.get(choose);
    }
}
