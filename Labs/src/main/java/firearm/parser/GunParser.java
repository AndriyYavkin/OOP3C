package firearm.parser;

import firearm.model.Firearms;

public interface GunParser {
    Firearms parse(String xmlFilePath) throws Exception;
}