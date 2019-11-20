import models.PlayerHand;
import models.Stone;

import java.util.*;

public class Main {

    private static final int COLOR_YELLOW = 0;
    private static final int COLOR_BLUE = 1;
    private static final int COLOR_BLACK = 2;
    private static final int COLOR_RED = 3;
    private static final int COLOR_FAKE_OKEY = 4;
    private static final Stone FAKE_OKEY = new Stone(52, 4);


    private static List<Stone> stones = new ArrayList<>();
    private static Stone indicator = new Stone();
    private static Stone okey = new Stone();

    public static void main(String[] args) {
        PlayerHand player1 = new PlayerHand();
        PlayerHand player2 = new PlayerHand();
        PlayerHand player3 = new PlayerHand();
        PlayerHand player4 = new PlayerHand();

        fillStonesAndChooseIndicatorAndSetOkey();

        List<PlayerHand> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        distributeStonesToPlayersAndSortStonesSmallToBig(player1, player2, player3, player4);

        findBestPlayerHand(players);

    }

    private static void findBestPlayerHand(List<PlayerHand> players) {
        int bestHandPoint = 0;
        int bestHandPlayer = 0;
        for (int i = 0; i < players.size(); i++) {

            int handPoint = calcPlayerHandPoint(players.get(i));

            if (handPoint > bestHandPoint) {
                bestHandPoint = handPoint;
                bestHandPlayer = i;
            } else if (handPoint == bestHandPoint) {
                if (players.get(i).getSerialPerCount() > players.get(bestHandPlayer).getSerialPerCount()) { // aynı puanda 3+ lı seri per sayısının fazla olması avantajlı durum olarak düşünülmüştür.
                    bestHandPlayer = i;
                } else if (players.get(i).getSerialPerCount() == players.get(bestHandPlayer).getSerialPerCount()) {
                    if (players.get(i).getSerialPerAndSameValueCountFor2Stones() > players.get(bestHandPlayer).getSerialPerAndSameValueCountFor2Stones()) {
                        bestHandPlayer = i;
                    } // eğer burdaki koşulda eşit ise ilk oynayacak olan yani sıralamada önde olan kişi daha erken taş çekeceği için onun daha avantajlı olduğu düşünülmüştür.
                }

            }
            // *************************
            //  Dağıtılan eller için oyuncularda bulunan taşları el sıralamalarını, yapılan 2li 3+ lı per sayılarını görmek için aşşağıdaki yorumu açabilirsiniz.
            // showHandAnalysis(players.get(i), handPoint,i+1);
        }

        System.out.println((bestHandPlayer + 1) + ". oyuncu " + bestHandPoint + " puan ile en iyi ele sahiptir.");
    }

    private static int calcPlayerHandPoint(PlayerHand playerHand) {
        findBestCombinationOfHand(playerHand);
        findTwinStonesPerList(playerHand);

        return Math.max(playerHand.getUsedPerStones().size(), playerHand.getTwinStones().size() * 2);
    }


    public static void showHandAnalysis(PlayerHand player, int handpoint, int playerNumber) {
        System.out.println();
        System.out.println(playerNumber + ". OYUNCUNUN EL ANALİZİ");
        System.out.println("ELDEKİ PERLER :");
        for (Stone stone : player.getUsedPerStones()) {
            System.out.println(stone.getValue());
        }
        System.out.println();

        System.out.println("KULLANILMAYAN TAŞLAR :");
        for (Stone stone : player.getUnUsedStones()) {
            System.out.println(stone.getValue());
        }
        System.out.println();

        System.out.println("ÇİFT TAŞLAR :");
        for (Stone stone : player.getTwinStones()) {
            System.out.println(stone.getValue());
        }
        System.out.println();

        System.out.println("EL puanı : " + handpoint);
        System.out.println("3 lü per sayısı :" + player.getSerialPerCount());
        System.out.println("2 li per ve aynı değer farklı renk per sayısı :" + player.getSerialPerAndSameValueCountFor2Stones());
        System.out.println();
        System.out.println("//////////////////////////");

    }

    private static void fillStonesAndChooseIndicatorAndSetOkey() {
        for (int i = 0; i < 53; i++) {
            Stone stone = new Stone();
            stone.setValue(i);
            stone.setColor(getColorOfStone(i));

            stones.add(stone);
        }
        stones.addAll(stones);
        Collections.shuffle(stones);

        int indicatorIndex = stones.get(0).getValue() == 52 ? (stones.get(1).getValue() == 52 ? 2 : 1) : 0; // sahte okey kontrolü
        indicator = stones.remove(indicatorIndex);


        okey = getNextStone(indicator);
    }


    private static void distributeStonesToPlayersAndSortStonesSmallToBig(PlayerHand player1, PlayerHand player2, PlayerHand player3, PlayerHand player4) {
        List<PlayerHand> players = new ArrayList<PlayerHand>(Arrays.asList(player1, player2, player3, player4));
        Collections.shuffle(players); // oyunculara random olarak eller veriliyor.

        players.get(0).setStones(stones.subList(0, 15));
        players.get(1).setStones(stones.subList(15, 29));
        players.get(2).setStones(stones.subList(29, 43));
        players.get(3).setStones(stones.subList(43, 57));

        sortStonesSmallToBig(players.get(0).getStones()); // Taşları küçükten büyüğe sıralıyoruz. Per sıralamalarında ve Çift bulmada bize kolaylık sağlaması için.
        sortStonesSmallToBig(players.get(1).getStones());
        sortStonesSmallToBig(players.get(2).getStones());
        sortStonesSmallToBig(players.get(3).getStones());

    }

    // gelen stone listeden silinipte yollanıyor.
    private static List<Stone> sortStoneForSameValueAndDiffColor(Stone stone, List<Stone> stones) {
        int stoneVal = stone.getValue();
        List<Stone> perList = new ArrayList<>();
        if (stone != okey) {
            perList.add(stone);
        } else {
            if (indexOf(FAKE_OKEY, stones) != -1) {
                perList.add(stones.get(indexOf(FAKE_OKEY, stones)));
            }
        }
        if (stone == FAKE_OKEY) {
            stone = okey;
        }
        while (stoneVal > 0) {
            Stone sameValue = findStoneFromValueInStoneList(stoneVal, stones);
            Stone tmpStone = checkOkeyAndNullBeforeAdd(sameValue, perList);
            if (tmpStone != null) {
                perList.add(tmpStone);
            }
            stoneVal -= 13;
        }
        stoneVal = stone.getValue() + 13;
        while (stoneVal < 52) {
            Stone sameValue = findStoneFromValueInStoneList(stoneVal, stones);
            Stone tmpStone = checkOkeyAndNullBeforeAdd(sameValue, perList);
            if (tmpStone != null) {
                perList.add(tmpStone);
            }
            stoneVal += 13;
        }
        return perList;
    }

    private static int indexOf(Stone stone, List<Stone> stones) {
        for (int i = 0; i < stones.size(); i++) {
            if (stone.getValue() == stones.get(i).getValue()) {
                return i;
            }
        }
        return -1;
    }


    private static void findBestCombinationOfHand(PlayerHand player) {


        List<Stone> stones = new ArrayList<>(player.getStones());

        while (stones.size() > 0) {

            List<Stone> perListForPer = new ArrayList<>();

            Stone checkStone = stones.get(0);
            stones.remove(checkStone);

            List<Stone> stmpStones = new ArrayList<>(stones);
            perListForPer.add(checkStone);

            sortStonesForSequentPer(checkStone, stmpStones, perListForPer);
            List<Stone> perListForSameValueAndDiffColor = new ArrayList<>(sortStoneForSameValueAndDiffColor(checkStone, stones));

            if (perListForPer.size() > 2 && perListForSameValueAndDiffColor.size() < 3) {
                player.getUsedPerStones().addAll(perListForPer);
                player.setSerialPerCount(player.getSerialPerCount() + 1);
                removeStonesFromStonesList(perListForPer, stones);
                stones.addAll(perListForSameValueAndDiffColor);  // TODO stone içindeyse sil eklemeden önce
            } else if (perListForSameValueAndDiffColor.size() > 2 && perListForPer.size() < 3) {
                player.getUsedPerStones().addAll(perListForSameValueAndDiffColor);
                removeStonesFromStonesList(perListForSameValueAndDiffColor, stones);
                stones.addAll(perListForPer);
            } else if (perListForPer.size() > 3 && perListForSameValueAndDiffColor.size() == 3 || perListForPer.size() > 2 && perListForSameValueAndDiffColor.size() == 4) {
                player.setSerialPerCount(player.getSerialPerCount() + 1);
                player.getUsedPerStones().addAll(perListForPer);
                player.getUsedPerStones().addAll(perListForSameValueAndDiffColor);
                removeStonesFromStonesList(perListForPer, stones);
                removeStonesFromStonesList(perListForSameValueAndDiffColor, stones);
            } else {
                if (perListForPer.size() == 2 || perListForSameValueAndDiffColor.size() == 2) {
                    player.setSerialPerAndSameValueCountFor2Stones(player.getSerialPerAndSameValueCountFor2Stones() + 1);
                }
                player.getUnUsedStones().add(checkStone);
            }
        }
    }

    private static void sortStonesSmallToBig(List<Stone> stones) {
        stones.sort(Comparator.comparingLong(Stone::getValue));
    }

    // yollanan listede ilk stone silinmiş geliyor
    private static void sortStonesForSequentPer(Stone stone, List<Stone> stones, List<Stone> perList) {
        Stone nextStone = getNextStone(stone);
        int indexOfNextStone = indexOf(nextStone, stones);
        if (indexOfNextStone != -1) {
            if (nextStone.getValue() % 13 == 0 && stone.getValue() % 13 == 12) { //12, 13, 1 kontrolü
                perList.add(stones.remove(indexOfNextStone));
                return;
            }
            perList.add(stones.remove(indexOfNextStone));
            sortStonesForSequentPer(nextStone, stones, perList);
        }
        if (nextStone.getValue() == okey.getValue()) {
            int indexOfFakeOkeyStone = indexOf(FAKE_OKEY, stones);
            if (indexOfFakeOkeyStone != -1) {
                perList.add(stones.remove(indexOfFakeOkeyStone));
                sortStonesForSequentPer(okey, stones, perList);
            }
        }

        if (indexOf(okey, stones) != -1) {
            Stone nextStoneFromOkey = getNextStone(nextStone);
            int indexOfNextStoneFromOkey = indexOf(nextStoneFromOkey, stones);
            if (indexOfNextStoneFromOkey != -1) {
                perList.add(stones.remove(indexOf(okey, stones)));
                perList.add(stones.remove(indexOfNextStoneFromOkey));
                sortStonesForSequentPer(nextStoneFromOkey, stones, perList);
            }
        }
    }

    private static void removeStonesFromStonesList(List<Stone> stone, List<Stone> stones) {
        for (Stone stone1 : stone) {
            stones.remove(stone1);
        }
    }

    private static Stone checkOkeyAndNullBeforeAdd(Stone stone, List<Stone> stones) {
        if (stone != null && indexOf(stone, stones) == -1) {
            if (stone != okey) {
                return stone;
            } else if (indexOf(FAKE_OKEY, stones) != -1) {
                return stones.get(indexOf(FAKE_OKEY, stones));
            }
        }
        return null;
    }

    private static Stone findStoneFromValueInStoneList(int val, List<Stone> stones) {
        return stones.stream().filter(x -> val == x.getValue()).findFirst().orElse(null);
    }


    private static Stone getNextStone(Stone stone) {

        Stone nextStone = new Stone();
        nextStone.setColor(stone.getColor());
        nextStone.setValue((stone.getValue() + 1) % 13 == 0 ? stone.getValue() - 12 : stone.getValue() + 1);
        return nextStone;
    }

    private static Stone getPrevStone(Stone stone) {
        Stone prevStone = new Stone();
        prevStone.setColor(stone.getColor());
        prevStone.setValue((stone.getValue() - 1) % 13 == 0 ? stone.getValue() + 12 : stone.getValue() - 1);

        return prevStone;
    }


    private static int getColorOfStone(Integer stone) {
        if (stone < 13) return COLOR_YELLOW;
        if (stone < 26) return COLOR_BLUE;
        if (stone < 39) return COLOR_BLACK;
        if (stone < 52) return COLOR_RED;

        return COLOR_FAKE_OKEY;
    }

    private static void findTwinStonesPerList(PlayerHand player) {
        List<Stone> stones = new ArrayList<>(player.getStones());
        for (int i = 0; i < stones.size(); i++) {
            Stone tmpStone = stones.get(i);
            if (okey.getValue() == tmpStone.getValue()) {
                player.getTwinStones().add(okey);
            } else if (i + 1 < stones.size() && tmpStone.getValue() == stones.get(i + 1).getValue()) {
                player.getTwinStones().add(tmpStone);
                i++;
            }
        }


    }
}
