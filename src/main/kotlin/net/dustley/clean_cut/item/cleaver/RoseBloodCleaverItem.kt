package net.dustley.clean_cut.item.cleaver

class RoseBloodCleaverItem : CarrionCleaverItem()
{

    override fun isRose() = true
    override fun getID(): String = "rose_blood_cleaver"
    override fun getBloodUIColor() = "#EBAFF2"

}