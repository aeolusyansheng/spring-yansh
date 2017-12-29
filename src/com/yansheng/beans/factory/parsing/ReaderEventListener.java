package com.yansheng.beans.factory.parsing;

import java.util.EventListener;

public interface ReaderEventListener extends EventListener {

	void defaultsRegistered(DefaultsDefinition defaultsDefinition);

	void componentRegistered(ComponentDefinition componentDefinition);

	void aliasRegistered(AliasDefinition aliasDefinition);

	void importProcessed(ImportDefinition importDefinition);

}
