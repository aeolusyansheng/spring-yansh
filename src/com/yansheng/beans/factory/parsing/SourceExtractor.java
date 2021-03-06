package com.yansheng.beans.factory.parsing;

import org.springframework.core.io.Resource;

public interface SourceExtractor {

	Object extractSource(Object sourceCandidate, Resource definingResource);
}
