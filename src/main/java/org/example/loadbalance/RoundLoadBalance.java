package org.example.loadbalance;

import java.util.List;

public class RoundLoadBalance implements LoadBalance{
    private int choose = -1;

    @Override
    public String balance(List<String> addressList) {
        choose++;
        choose = choose % addressList.size();
        System.out.println(this.getClass().getName());
        return addressList.get(choose);
    }
}
