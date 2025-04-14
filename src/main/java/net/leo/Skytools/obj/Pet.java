package net.leo.Skytools.obj;

import net.minecraft.resources.ResourceLocation;

public class Pet {
    public String petName;
    public String rarity;
    public int level;
    public String progress;
    public String literalName;
    public ResourceLocation petImage;

    public Pet() {
        this.petName = "";
        this.rarity = "§f";
        this.level = 0;
        this.progress = "0/0 XP (100%)";
        this.literalName = "";
    }

    public Pet(String name, int level) {
        this.petName = name;
        this.rarity = "§f";
        this.level = level;
        this.progress = "0/0 XP (100%)";
        this.literalName = filterLiteralName();
        this.petImage = getImage();
    }

    public Pet(String name, String rarity, int level) {
        this.petName = name;
        this.rarity = rarity;
        this.level = level;
        this.progress = "0/0 XP (100%)";
        this.literalName = filterLiteralName();
        this.petImage = getImage();
    }

    public Pet(String name, String rarity, int level, String progress) {
        this.petName = name;
        this.rarity = rarity;
        this.level = level;
        this.progress = changeMax(progress);
        this.literalName = filterLiteralName();
        this.petImage = getImage();
    }

    public void changePet(Pet newPet) {
        this.petName = newPet.petName;
        this.rarity = newPet.rarity;
        this.level = newPet.level;
        this.progress = newPet.progress;
        this.literalName = newPet.literalName;
        this.petImage = newPet.petImage;
    }

    public String filterLiteralName() {
        int cutIndex = this.petName.indexOf("§");
        if (cutIndex != -1) {
            return this.petName.substring(0, cutIndex).trim();
        }
        return this.petName;
    }

    public boolean isValidPet() {
        if(this.petName.isEmpty())
            return false;
        return true;
    }

    public String displayPet() {
        return this.level == 0 ? "" : ("§7[Lvl " + this.level + "] " + this.rarity + this.petName);
    }

    public String displayPetInfo() {
        return this.level == 0 ? "" : ("§7[Lvl " + this.level + "] " + this.rarity + this.petName + "\n" + this.progress);
    }

    public ResourceLocation getImage() {
        if(!isValidPet()) {
            return ResourceLocation.fromNamespaceAndPath(
                    "skytools",
                    "textures/pets/" +
                            "skyblock_pets_" +
                            ".png"
            );
        }
        return ResourceLocation.fromNamespaceAndPath(
                "skytools",
                "textures/pets/" +
                        "skyblock_pets_" +
                        literalName.replace(" ", "_").toLowerCase() +
                        ".png"
        );
    }

    public String getImageName() {
        return "skyblock_pets_" + literalName.replace(" ", "_").toLowerCase() + ".png";
    }

    public String changeMax(String progress) {
        if(progress.equals("MAX LEVEL"))
            return "§b§l" + progress;
        return progress;
    }
}
