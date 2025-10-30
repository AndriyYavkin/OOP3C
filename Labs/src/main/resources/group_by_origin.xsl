<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:tns="http://www.firearm.com/schema">

    <xsl:output method="xml" indent="yes"/>

    <xsl:key name="guns-by-origin" match="tns:Gun" use="tns:Origin"/>

    <xsl:template match="/tns:Firearms">
        <root>
            <xsl:apply-templates select="tns:Gun[
                count(. | key('guns-by-origin', tns:Origin)[1]) = 1
            ]" mode="group">
                <xsl:sort select="tns:Origin"/>
            </xsl:apply-templates>
        </root>
    </xsl:template>

    <xsl:template match="tns:Gun" mode="group">
        <Group>
            <xsl:attribute name="origin">
                <xsl:value-of select="tns:Origin"/>
            </xsl:attribute>
            
            <xsl:for-each select="key('guns-by-origin', tns:Origin)">
                <xsl:copy-of select="."/>
            </xsl:for-each>
        </Group>
    </xsl:template>

</xsl:stylesheet>