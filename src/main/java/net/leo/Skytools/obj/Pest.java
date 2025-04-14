package net.leo.Skytools.obj;

import net.leo.Skytools.util.SkyCommand;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Pest {
    public int alive;
    public Set<Integer> plots;
    public String spray;
    public String repellent;
    public String bonus;
    public String cooldown;

    public Pest() {
        this.alive = 0;
        this.plots = new HashSet<>();
        this.spray = "§7None";
        this.repellent = "§7None";
        this.bonus = "§c§lINACTIVE";
        this.cooldown = "§a§lREADY";
    }

    public Pest(int alive, String spray, String repellent, String bonus, String cooldown) {
        this.alive = alive;
        this.plots = new HashSet<>();
        this.spray = spray;
        this.repellent = repellent;
        this.bonus = isBonusDefault(bonus);
        this.cooldown = isCooldownDefault(cooldown);
    }

    public Pest(int alive, Set<Integer> plots, String spray, String repellent, String bonus, String cooldown) {
        this.alive = alive;
        this.plots = plots != null ? plots : new HashSet<>();
        this.spray = spray;
        this.repellent = repellent;
        this.bonus = isBonusDefault(bonus);
        this.cooldown = isCooldownDefault(cooldown);
    }

    public Pest(int alive, String plots, String spray, String repellent, String bonus, String cooldown) {
        this.alive = alive;
        this.plots = new HashSet<>();
        setPlot(plots);
        this.spray = spray;
        this.repellent = repellent;
        this.bonus = isBonusDefault(bonus);
        this.cooldown = isCooldownDefault(cooldown);
    }

    public String isBonusDefault(String bonus) {
        if(bonus.equals("INACTIVE")) {
            return "§c§l" + bonus;
        }
        return bonus;
    }

    public boolean isFarmingDebuff() {
        return this.alive >= 4;
    }

    public String isCooldownDefault(String cooldown) {
        if(cooldown.equals("READY")) {
            return "§a§l" + cooldown;
        }
        return cooldown;
    }

    public void setPlot(Set<Integer> newPlots) {
        if (newPlots != null) {
            this.plots.clear();
            this.plots.addAll(newPlots);
        }
    }

    public void setPlot(String plotsStr) {
        this.plots.clear();
        if (plotsStr == null || plotsStr.isEmpty()) return;

        // Remove Minecraft color codes (e.g., §b, §f)
        plotsStr = plotsStr.replaceAll("§.", "");

        String[] split = plotsStr.split(",");
        for (String s : split) {
            try {
                int plot = Integer.parseInt(s.trim());
                this.plots.add(plot);
            } catch (NumberFormatException ignored) {
                // Optionally log or handle bad inputs
            }
        }
    }


    public void changePest(Pest newPest) {
        this.alive = newPest.alive;
        this.plots = new HashSet<>(newPest.plots);
        this.spray = newPest.spray;
        this.repellent = newPest.repellent;
        this.bonus = newPest.bonus;
        this.cooldown = newPest.cooldown;
    }

    public String getPlots() {
        if (plots.isEmpty())
            return "§7None";
        return plots.stream()
                .map(p -> "§b" + p + "§f")
                .collect(Collectors.joining(", "));
    }

    public void tp2Pest() {
        if (this.plots == null || this.plots.isEmpty()) {
            SkyCommand.sendClientChatMessage("§4§l<!> No Pest Found <!>");
            return;
        }

        // You can choose either first or last depending on preference
        // For example, getting the first element:
        Integer plot = this.plots.iterator().next();

        if (plot != null) {
            SkyCommand.sendChatCommand("tptoplot " + plot);
        } else {
            SkyCommand.sendClientChatMessage("§4§l<!> No Valid Plot <!>");
        }
    }

    @Override
    public String toString() {
        return "alive=" + alive +
                ", plots=" + plots +
                ", spray='" + spray + '\'' +
                ", repellent='" + repellent + '\'' +
                ", bonus='" + bonus + '\'' +
                ", cooldown='" + cooldown + '\'';
    }

    public String displayPetInfo() {
        return "alive=" + alive +
                ", plots=" + plots +
                ", spray='" + spray + '\'' +
                ", repellent='" + repellent + '\'' +
                ", bonus='" + bonus + '\'' +
                ", cooldown='" + cooldown + '\'';
    }
}
