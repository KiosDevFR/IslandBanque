package org.cubitech.islandbanque.managers;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.cubitech.islandbanque.IslandBanque;
import org.cubitech.islandbanque.objects.*;
import org.cubitech.islandbanque.utils.UtilsBanque;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class DatabaseManager {
    private static DatabaseManager databaseManager;
    private Connection connection;

    private DatabaseManager(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + IslandBanque.getInstance().getDataFolder() + "/database.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        /*Creates table of islands claimed interest*/
        try (Statement stmt = connection.createStatement();){
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS islandsclaimed(uuid VARCHAR(36) NOT NULL PRIMARY KEY);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        /*Creates table of islands*/
        try (Statement stmt = connection.createStatement();){
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS islandsbanks(uuid VARCHAR(36) NOT NULL PRIMARY KEY," +
                    "money BIGINT NOT NULL," +
                    "xp BIGINT NOT NULL," +
                    "farmpoints BIGINT NOT NULL," +
                    "interestmoney DOUBLE NOT NULL," +
                    "limitmoney BIGINT NOT NULL," +
                    "limitxp BIGINT NOT NULL," +
                    "limitfarmpoints BIGINT NOT NULL" +
                    ");");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        try (Statement stmt = connection.createStatement();){
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS transactions(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "type INTEGER NOT NULL," + // (0 money - 1 xp - 2 farmpoints)
                    "amount BIGINT NOT NULL," +
                    "uuid_island VARCHAR(36) NOT NULL," +
                    "uuid_player VARCHAR(36) NOT NULL," +
                    "totalamountafter BIGINT NOT NULL," +
                    "date TEXT NOT NULL," +
                    "player_name TEXT NOT NULL" +
                    ");");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //This table stores the amount of each currency that a player has added to an island bank
        try (Statement stmt = connection.createStatement();){
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS playersislands(uuid_island VARCHAR(36) NOT NULL," +
                    "uuid_player VARCHAR(36) NOT NULL," +
                    "money BIGINT NOT NULL," +
                    "xp BIGINT NOT NULL," +
                    "farmpoints BIGINT NOT NULL," +
                    "PRIMARY KEY(uuid_island, uuid_player)" +
                    ");");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean hasClaimedInterest(SuperiorPlayer player){
        return hasClaimedInterest(player.getIsland());
    }

    public boolean hasClaimedInterest(Island island){
        ResultSet rs = null;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) as result FROM " +
                        "islandsclaimed WHERE uuid = ?;")){
            stmt.setString(1,island.getUniqueId().toString());
            rs = stmt.executeQuery();
            rs.next();
            return rs.getInt("result") == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void restartClaimedInterests(){
        try (Statement stmt = connection.createStatement();){
            stmt.executeUpdate("DELETE FROM islandsclaimed;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Claims interest for player island.
     * @param player
     */
    public void claimInterest(SuperiorPlayer player) {
        claimInterest(player.getIsland());
    }

    /**
     * Claims interest for island.
     * @param island
     */
    public void claimInterest(Island island) {
        Bank previousBankIsland = getIslandBank(island);
        ResultSet rs = null;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT interestmoney,limitmoney,limitxp,limitfarmpoints " +
                "FROM islandsbanks WHERE uuid = ?;");){

            stmt.setString(1,island.getUniqueId().toString());

            rs = stmt.executeQuery();
            if(rs.next()){
                double interestMoney = rs.getDouble("interestmoney");
                try(PreparedStatement stmt2 = connection.prepareStatement("UPDATE islandsbanks " +
                        "SET money = ?, xp = ?, farmpoints = ? " +
                        "WHERE uuid = ?;");){
                    long limitMoney = rs.getLong("limitmoney");
                    long limitXp = rs.getLong("limitxp");
                    long limitFarmpoints = rs.getLong("limitfarmpoints");


                    long estimatedMoneyFromInterest = (long)((double)previousBankIsland.getMoney() * interestMoney / 100d);
                    long estimatedXpFromInterest = (long)((double)previousBankIsland.getExp() * interestMoney / 100d);
                    long estimatedFarmPointsFromInterest = (long)((double)previousBankIsland.getFarmPoints() * interestMoney / 100d);


                    if(estimatedMoneyFromInterest > limitMoney){
                        estimatedMoneyFromInterest = limitMoney;
                    }
                    if(estimatedXpFromInterest > limitXp){
                        estimatedXpFromInterest = limitXp;
                    }
                    if(estimatedFarmPointsFromInterest > limitFarmpoints){
                        estimatedFarmPointsFromInterest = limitFarmpoints;
                    }


                    stmt2.setLong(1, previousBankIsland.getMoney() + estimatedMoneyFromInterest);
                    stmt2.setLong(2, previousBankIsland.getExp() + estimatedXpFromInterest);
                    stmt2.setLong(3, previousBankIsland.getFarmPoints() + estimatedFarmPointsFromInterest);
                    stmt2.setString(4,island.getUniqueId().toString());
                    stmt2.executeUpdate();

                    addIslandToClaimedInterest(island);
                }


            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void addIslandToClaimedInterest(Island island){
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO islandsclaimed(uuid) VALUES(?);");){
            stmt.setString(1,island.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Bank getIslandBank(Island island){
        ResultSet rs = null;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT money,xp,farmpoints " +
                "FROM islandsbanks WHERE uuid = ?;");){

            stmt.setString(1,island.getUniqueId().toString());

            rs = stmt.executeQuery();
            if(rs.next()){
                return new Bank(island,rs.getLong("money"),rs.getLong("xp"),rs.getLong("farmpoints"));
            }else{
                return createIslandBank(island);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Creates island bank
     * @param island
     * @return
     */
    private Bank createIslandBank(Island island) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO islandsbanks(uuid,money,xp,farmpoints,interestmoney,limitmoney,limitxp,limitfarmpoints) " +
                "VALUES(?,?,?,?,?,?,?,?);");){
            Bank bank = new Bank(island, 0,0,0);
            stmt.setString(1,bank.getIsland().getUniqueId().toString());
            stmt.setLong(2,bank.getMoney());
            stmt.setLong(3,bank.getExp());
            stmt.setLong(4,bank.getFarmPoints());
            stmt.setDouble(5,IslandBanque.getInstance().getConfig().getDouble("Config.default_interet"));
            stmt.setLong(6,IslandBanque.getInstance().getConfig().getLong("Config.default_limite_argent"));
            stmt.setLong(7,IslandBanque.getInstance().getConfig().getLong("Config.default_limite_exp"));
            stmt.setLong(8,IslandBanque.getInstance().getConfig().getLong("Config.default_limite_farmpoints"));
            stmt.executeUpdate();

            return bank;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseManager getInstance(){
        if(databaseManager == null){
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public static void setUpDatabase(){
        DatabaseManager.getInstance();
    }

    /**
     * Adds balance to an island on the database.
     * @param island
     * @param type
     * @param amount
     * @return Returns previous bank before the new values.
     */
    public Bank addBalanceToIsland(Island island, int type, long amount) {
        Bank bank = getIslandBank(island); /*This allows us to get the state of the bank before updating the balance, and creates the
                                            bank for the island if it doesn't have one*/
        try(PreparedStatement ps = connection.prepareStatement("UPDATE islandsbanks " +
                "SET "+ UtilsBanque.getStringFromType(type) + " = " + UtilsBanque.getStringFromType(type) + " + ? " +
                "WHERE uuid = ?;")){
            ps.setLong(1,amount);
            ps.setString(2,island.getUniqueId().toString());
            ps.executeUpdate();
            return bank;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets balance of island.
     * @param island
     * @param type
     * @param amount
     */
    public void setIslandBalance(Island island, int type, long amount) {
        getIslandBank(island); /*This allows us to get the state of the bank before updating the balance, and creates the
                               bank for the island if it doesn't have one*/
        try(PreparedStatement ps = connection.prepareStatement("UPDATE islandsbanks " +
                "SET "+ UtilsBanque.getStringFromType(type) + " = ? " +
                "WHERE uuid = ?;")){
            ps.setLong(1,amount);
            ps.setString(2,island.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a transaction.
     * @param player
     * @param type
     * @param amount
     */
    public void generateTransaction(SuperiorPlayer player, int type, long amount) {
        Bank previousBank = IslandManager.getInstance().getIslandBank(player.getIsland());
        try(PreparedStatement ps = connection.prepareStatement("INSERT INTO transactions(type,amount,uuid_island,uuid_player,totalamountafter,date,player_name) " +
                "VALUES (?,?,?,?,?,?,?)")){
            ps.setInt(1,type);
            ps.setLong(2,amount);
            ps.setString(3,player.getIsland().getUniqueId().toString());
            ps.setString(4, player.getUniqueId().toString());
            ps.setLong(5,switch (type){
                case 0 -> previousBank.getMoney();
                case 1 -> previousBank.getExp();
                case 2 -> previousBank.getFarmPoints();
                default -> throw new RuntimeException("UNEXPECTED VALUE");
            });
            ps.setString(6, LocalDateTime.now().toString());
            ps.setString(7, player.getName());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        addPlayerAddings(player,player.getIsland(),type,amount);

        deleteExtraTransactions(player,type);
    }

    private void deleteExtraTransactions(SuperiorPlayer player, int type) {
        try(PreparedStatement ps = connection.prepareStatement("DELETE FROM transactions WHERE type = ? AND uuid_island = ? AND id NOT IN(" +
                "SELECT id FROM transactions ORDER BY id DESC LIMIT "+IslandBanque.getInstance().getConfig().getInt("Config.max_logs")+");")){
            ps.setInt(1,type);
            ps.setString(2,player.getIsland().getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets island logs for Money
     *
     * @param island
     * @return
     */
    public ArrayList<BanqueLogMoney> getIslandMoneyLogs(Island island) {
        ArrayList<BanqueLogMoney> logs = new ArrayList<>();
        ResultSet rs = null;
        try(PreparedStatement ps = connection.prepareStatement("SELECT amount,uuid_island,uuid_player,totalamountafter,date,player_name FROM transactions WHERE type = 0 AND uuid_island = ? ORDER BY id DESC;")){
            ps.setString(1,island.getUniqueId().toString());
            rs = ps.executeQuery();

            while(rs.next()){
                logs.add(new BanqueLogMoney(rs.getLong("amount"),
                        SuperiorSkyblockAPI.getIslandByUUID(UUID.fromString(rs.getString("uuid_island"))),
                        rs.getLong("totalamountafter"),
                        LocalDateTime.parse(rs.getString("date")),
                        rs.getString("player_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return logs;
    }
    /**
     * Gets island logs for Xp
     *
     * @param island
     * @return
     */
    public ArrayList<BanqueLogXp> getIslandXpLogs(Island island) {
        ArrayList<BanqueLogXp> logs = new ArrayList<>();
        ResultSet rs = null;
        try(PreparedStatement ps = connection.prepareStatement("SELECT amount,uuid_island,uuid_player,totalamountafter,date,player_name FROM transactions WHERE type = 1 AND uuid_island = ? ORDER BY id DESC;")){
            ps.setString(1,island.getUniqueId().toString());
            rs = ps.executeQuery();

            while(rs.next()){
                logs.add(new BanqueLogXp(rs.getLong("amount"),
                        SuperiorSkyblockAPI.getIslandByUUID(UUID.fromString(rs.getString("uuid_island"))),
                        rs.getLong("totalamountafter"),
                        LocalDateTime.parse(rs.getString("date")),
                        rs.getString("player_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return logs;
    }
    /**
     * Gets island logs for Farmpoints
     *
     * @param island
     * @return
     */
    public ArrayList<BanqueLogFarmpoints> getIslandFarmpointsLogs(Island island) {
        ArrayList<BanqueLogFarmpoints> logs = new ArrayList<>();
        ResultSet rs = null;
        try(PreparedStatement ps = connection.prepareStatement("SELECT amount,uuid_island,uuid_player,totalamountafter,date,player_name FROM transactions WHERE type = 2 AND uuid_island = ? ORDER BY id DESC;")){
            ps.setString(1,island.getUniqueId().toString());
            rs = ps.executeQuery();

            while(rs.next()){
                logs.add(new BanqueLogFarmpoints(rs.getLong("amount"),
                        SuperiorSkyblockAPI.getIslandByUUID(UUID.fromString(rs.getString("uuid_island"))),
                        rs.getLong("totalamountafter"),
                        LocalDateTime.parse(rs.getString("date")),
                        rs.getString("player_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return logs;
    }

    /**
     * Gets total player addings to an Island.
     * @param player
     * @param island
     * @return Current player addings.
     */
    public PlayerAddings getPlayerAddings(SuperiorPlayer player, Island island){
        ResultSet rs = null;
        try(PreparedStatement ps = connection.prepareStatement("SELECT money,xp,farmpoints FROM playersislands " +
                "WHERE uuid_island = ? AND uuid_player = ?;")){
            ps.setString(1,island.getUniqueId().toString());
            ps.setString(2,player.getUniqueId().toString());
            rs = ps.executeQuery();
            if(rs.next()){
                return new PlayerAddings(player,rs.getLong("money"),rs.getLong("xp"),rs.getLong("farmpoints"));
            }else{
                return createPlayerAddings(player,island);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private PlayerAddings createPlayerAddings(SuperiorPlayer player, Island island) {
        try(PreparedStatement ps = connection.prepareStatement("INSERT INTO playersislands(uuid_island,uuid_player,money,xp,farmpoints) VALUES(?,?,?,?,?);")){
            PlayerAddings playerAddings = new PlayerAddings(player,0,0,0);
            ps.setString(1,island.getUniqueId().toString());
            ps.setString(2,player.getUniqueId().toString());
            ps.setLong(3,0);
            ps.setLong(4,0);
            ps.setLong(5,0);
            ps.executeUpdate();

            return playerAddings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the playerAddings on table playersislands.
     * @param player
     * @param island
     * @param type
     * @param amount
     * @return Returns previous PlayerAddings before the new values.
     */
    private PlayerAddings addPlayerAddings(SuperiorPlayer player, Island island,int type, long amount) {
        PlayerAddings playerAddings = getPlayerAddings(player,island);
        try(PreparedStatement ps = connection.prepareStatement("UPDATE playersislands " +
                "SET " + UtilsBanque.getStringFromType(type) + " = " + UtilsBanque.getStringFromType(type) + " + ? " +
                "WHERE uuid_island = ? AND uuid_player = ?;")){
            ps.setLong(1,amount);
            ps.setString(2,island.getUniqueId().toString());
            ps.setString(3,player.getUniqueId().toString());
            ps.executeUpdate();
            return playerAddings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets interest limit for an island.
     * @param island
     * @param type
     * @param amount
     */
    public void setInterestLimit(Island island, int type, long amount) {
        getIslandBank(island);
        try(PreparedStatement ps = connection.prepareStatement("UPDATE islandsbanks " +
                "SET limit" + UtilsBanque.getStringFromType(type) + " = ? " +
                "WHERE uuid = ?;")){
            ps.setLong(1,amount);
            ps.setString(2,island.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets money interest limit for an island.
     * @param island
     */
    public long getInterestLimitMoney(Island island) {
        ResultSet rs = null;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT limitmoney " +
                "FROM islandsbanks WHERE uuid = ?;")) {
            stmt.setString(1,island.getUniqueId().toString());
            rs = stmt.executeQuery();
            rs.next();
            return rs.getLong("limitmoney");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Gets xp interest limit for an island.
     * @param island
     */
    public long getInterestLimitXp(Island island) {
        ResultSet rs = null;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT limitxp " +
                "FROM islandsbanks WHERE uuid = ?;")) {
            stmt.setString(1,island.getUniqueId().toString());
            rs = stmt.executeQuery();
            rs.next();
            return rs.getLong("limitxp");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Gets farmpoints interest limit for an island.
     * @param island
     */
    public long getInterestLimitFarmpoints(Island island) {
        ResultSet rs = null;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT limitfarmpoints " +
                "FROM islandsbanks WHERE uuid = ?;")) {
            stmt.setString(1,island.getUniqueId().toString());
            rs = stmt.executeQuery();
            rs.next();
            return rs.getLong("limitfarmpoints");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Sets intererst of an island.
     * @param island
     * @param amount
     */
    public void setInterest(Island island,double amount) {
        getIslandBank(island);
        try(PreparedStatement ps = connection.prepareStatement("UPDATE islandsbanks " +
                "SET interestmoney = ? " +
                "WHERE uuid = ?;")){
            ps.setDouble(1,amount);
            ps.setString(2,island.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets intererst of an island.
     * @param island
     */
    public double getInterestMoney(Island island) {
        getIslandBank(island);
        ResultSet rs = null;
        try(PreparedStatement ps = connection.prepareStatement("SELECT interestmoney FROM islandsbanks " +
                "WHERE uuid = ?;")){
            ps.setString(1,island.getUniqueId().toString());
            rs = ps.executeQuery();
            rs.next();
            return rs.getDouble("interestmoney");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


}
