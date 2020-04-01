package com.company;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class MainTest {

    @Test
    public void transformation() {
        assertEquals("filter{(element<0)&(element>0)}%>%map{((element+7)*element)}", Main.transform("filter{(element>0)}%>%filter{(element<0)}%>%map{((element+7)*element)}", "element", "1=1"));
        assertEquals("filter{(element>0)}%>%map{((element+7)*element)}", Main.transform("filter{(element>0)}%>%map{((element+7)*element)}", "element", "1=1"));
        assertEquals("SYNTAX ERROR", Main.transform("afilter{(element>0)}%>%map{((element+7)*element)}", "element", "1=1"));
        assertEquals("TYPE ERROR", Main.transform("9", "element", "1=1"));
    }

    @Test
    public void syntax(){
        assertTrue(Main.check_expression("((element<-19)&((element>0)|0))"));
        assertFalse(Main.check_expression("dk"));
    }

}