package gg.steve.elemental.gangs.bank;

import gg.steve.elemental.gangs.core.Gang;
import gg.steve.elemental.gangs.core.GangManager;

import java.util.ArrayList;
import java.util.List;

public class BankCalculation {
    private static List<Gang> gangsInOrder;

    public static List<Gang> getGangsInBankOrder() {
        if (gangsInOrder == null) gangsInOrder = new ArrayList<>();
        for (Gang gang : GangManager.getGangs().values()) {
            if (!gangsInOrder.contains(gang)) gangsInOrder.add(gang);
        }
        gangsInOrder.sort(new GangComparator());
        return gangsInOrder;
    }

    public static int getGangPosition(Gang gang) {
        if (gangsInOrder == null || !gangsInOrder.contains(gang)) getGangsInBankOrder();
        for (int i = 0; i < gangsInOrder.size(); i++) {
            if (gangsInOrder.get(i).getGangId().equals(gang.getGangId())) return i + 1;
        }
        return gangsInOrder.size();
    }

    public static void removeGang(Gang gang) {
        if (gangsInOrder == null) getGangsInBankOrder();
        if (gangsInOrder.contains(gang)) {
            gangsInOrder.remove(gang);
            getGangsInBankOrder();
        }
    }
}
