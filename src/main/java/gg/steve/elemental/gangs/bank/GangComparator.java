package gg.steve.elemental.gangs.bank;

import gg.steve.elemental.gangs.core.Gang;

import java.util.Comparator;

public class GangComparator implements Comparator<Gang> {

    @Override
    public int compare(Gang o1, Gang o2) {
        return (int) (o2.getBank() - o1.getBank());
    }
}
