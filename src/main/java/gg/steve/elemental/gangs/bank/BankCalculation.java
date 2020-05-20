package gg.steve.elemental.gangs.bank;

import gg.steve.elemental.gangs.core.Gang;
import gg.steve.elemental.gangs.core.GangManager;

import java.util.ArrayList;
import java.util.List;

public class BankCalculation {

    public static List<Gang> getGangsInBankOrder() {
        List<Gang> bank = new ArrayList<>();
        for (Gang gang : GangManager.getGangs().values()) {
            bank.add(gang);
        }
        bank.sort(new GangComparator());
        return bank;
    }
}
