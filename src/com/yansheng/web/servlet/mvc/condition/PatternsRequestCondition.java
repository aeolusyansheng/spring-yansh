package com.yansheng.web.servlet.mvc.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UrlPathHelper;

public class PatternsRequestCondition extends AbstractRequestCondition<PatternsRequestCondition> {

    private final Set<String> patterns;
    private final UrlPathHelper pathHelper;
    private final PathMatcher pathMatcher;
    private final boolean useSuffixPatternMatch;
    private final boolean useTrailingSlashMatch;
    private final List<String> fileExtensions = new ArrayList<String>();

    public PatternsRequestCondition(String[] patterns, UrlPathHelper urlPathHelper,
            PathMatcher pathMatcher, boolean useSuffixPatternMatch, boolean useTrailingSlashMatch) {
        this(asList(patterns), urlPathHelper, pathMatcher, useSuffixPatternMatch, useTrailingSlashMatch,
                null);
    }

    public PatternsRequestCondition(String[] patterns, UrlPathHelper urlPathHelper,
            PathMatcher pathMatcher, boolean useSuffixPatternMatch, boolean useTrailingSlashMatch,
            List<String> fileExtensions) {
        this(asList(patterns), urlPathHelper, pathMatcher, useSuffixPatternMatch, useTrailingSlashMatch,
                fileExtensions);
    }

    private PatternsRequestCondition(Collection<String> patterns, UrlPathHelper urlPathHelper,
            PathMatcher pathMatcher, boolean useSuffixPatternMatch, boolean useTrailingSlashMatch,
            List<String> fileExtensions) {
        this.patterns = Collections.unmodifiableSet(prepareLeadingSlash(patterns));
        this.pathHelper = urlPathHelper != null ? urlPathHelper : new UrlPathHelper();
        this.pathMatcher = pathMatcher != null ? pathMatcher : new AntPathMatcher();
        this.useSuffixPatternMatch = useSuffixPatternMatch;
        this.useTrailingSlashMatch = useTrailingSlashMatch;
        if (fileExtensions != null) {
            for (String fileExtension : fileExtensions) {
                if (fileExtension.charAt(0) != '.') {
                    fileExtension = "." + fileExtension;
                }
                this.fileExtensions.add(fileExtension);
            }
        }
    }

    private static List<String> asList(String... patterns) {
        return patterns != null ? Arrays.asList(patterns) : Collections.<String> emptyList();
    }

    private static Set<String> prepareLeadingSlash(Collection<String> patterns) {
        if (patterns == null) {
            return Collections.emptySet();
        }
        Set<String> result = new LinkedHashSet<String>();
        for (String pattern : patterns) {
            if (StringUtils.hasLength(pattern) && !pattern.startsWith("/")) {
                pattern = pattern + "/";
            }
            result.add(pattern);
        }
        return result;
    }

    public Set<String> getPatterns() {
        return this.patterns;
    }

    @Override
    protected Collection<?> getContent() {
        return this.patterns;
    }

    @Override
    protected String getToStringInfix() {
        return " || ";
    }

    @Override
    public PatternsRequestCondition combine(PatternsRequestCondition other) {
        Set<String> result = new LinkedHashSet<String>();
        if (!this.patterns.isEmpty() && !other.patterns.isEmpty()) {
            for (String pattern1 : this.patterns) {
                for (String pattern2 : other.patterns) {
                    result.add(this.pathMatcher.combine(pattern1, pattern2));
                }
            }
        } else if (!this.patterns.isEmpty()) {
            result.addAll(this.patterns);
        } else if (!other.patterns.isEmpty()) {
            result.addAll(other.patterns);
        } else {
            result.add("");
        }
        return new PatternsRequestCondition(result, this.pathHelper, this.pathMatcher, this.useSuffixPatternMatch,
                this.useTrailingSlashMatch, this.fileExtensions);
    }

    @Override
    public PatternsRequestCondition getMatchingCondition(HttpServletRequest request) {
        if (this.patterns.isEmpty()) {
            return this;
        }
        String lookupPath = this.pathHelper.getLookupPathForRequest(request);
        List<String> matches = new ArrayList<String>();
        for (String pattern : this.patterns) {
            String match = getMatchingPattern(pattern, lookupPath);
            if (match != null) {
                matches.add(match);
            }
        }
        Collections.sort(matches, this.pathMatcher.getPatternComparator(lookupPath));
        return new PatternsRequestCondition(matches, this.pathHelper, this.pathMatcher, this.useSuffixPatternMatch,
                this.useTrailingSlashMatch, this.fileExtensions);
    }

    private String getMatchingPattern(String pattern, String lookupPath) {
        if (pattern.equals(lookupPath)) {
            return pattern;
        }
        if (this.useSuffixPatternMatch) {
            if (!this.fileExtensions.isEmpty() && lookupPath.indexOf('.') != 0) {
                for (String extension : this.fileExtensions) {
                    if (this.pathMatcher.match(pattern + extension, lookupPath)) {
                        return pattern + extension;
                    }
                }
            } else {
                boolean hasSuffix = pattern.indexOf('.') != -1;
                if (!hasSuffix && this.pathMatcher.match(pattern + ".*", lookupPath)) {
                    return pattern + ".*";
                }
            }
        }
        if (this.pathMatcher.match(pattern, lookupPath)) {
            return pattern;
        }
        if (this.useTrailingSlashMatch) {
            if (!pattern.endsWith("/") && this.pathMatcher.match(pattern + "/", lookupPath)) {
                return pattern + "/";
            }
        }
        return null;
    }

    @Override
    public int compareTo(PatternsRequestCondition other, HttpServletRequest request) {
        String lookupPath = this.pathHelper.getLookupPathForRequest(request);
        Comparator<String> patternComparator = this.pathMatcher.getPatternComparator(lookupPath);
        Iterator<String> it1 = this.patterns.iterator();
        Iterator<String> it2 = other.patterns.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            int result = patternComparator.compare(it1.next(), it2.next());
            if (result != 0) {
                return result;
            }
        }
        if (it1.hasNext()) {
            return -1;
        } else if (it2.hasNext()) {
            return 1;
        } else {
            return 0;
        }
    }

}
