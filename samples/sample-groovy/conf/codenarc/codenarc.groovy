ruleset {
    description '全局默认CodeNarc规则集'

    ruleset('rulesets/basic.xml')
    ruleset('rulesets/braces.xml')
    ruleset('rulesets/comments.xml')
    ruleset('rulesets/concurrency.xml')
    ruleset('rulesets/design.xml') {
        'Instanceof' priority: 4
    }
    ruleset('rulesets/dry.xml') {
        'DuplicateMapLiteral' priority: 4, doNotApplyToFilesMatching: /.*(FT|IT|UT|Test)_?\d*\.groovy/
        'DuplicateNumberLiteral' priority: 4, doNotApplyToFilesMatching: /.*(FT|IT|UT|Test)_?\d*\.groovy/
        'DuplicateStringLiteral' priority: 4, doNotApplyToFilesMatching: /.*(FT|IT|UT|Test)_?\d*\.groovy/
    }
    ruleset('rulesets/exceptions.xml')
    ruleset('rulesets/formatting.xml') {
        'LineLength' ignoreLineRegex: /.*'.*'.*|.*".*".*|.*测试.*|class .*/
        'ConsecutiveBlankLines' enabled: false
        'SpaceAroundMapEntryColon' characterBeforeColonRegex: /\s|\w|\)|'|"|[\u4e00-\u9fa5]/, characterAfterColonRegex: /\s/
    }
    ruleset('rulesets/generic.xml')
    ruleset('rulesets/grails.xml')
    ruleset('rulesets/groovyism.xml')
    ruleset('rulesets/imports.xml') {
        'MisorderedStaticImports' comesBefore: false
    }
    ruleset('rulesets/jdbc.xml')
    ruleset('rulesets/junit.xml') {
        'JUnitPublicNonTestMethod' priority: 4
    }
    ruleset('rulesets/logging.xml')
    ruleset('rulesets/naming.xml') {
        'FieldName' staticFinalRegex: '[A-Z][A-Z0-9_]*', staticRegex: '[a-z][a-zA-Z0-9_]*', ignoreFieldNames: 'serialVersionUID'
        'MethodName' ignoreMethodNames: '*测试*,*test*'
        'PropertyName' staticFinalRegex: '[A-Z][A-Z0-9_]*', staticRegex: '[a-z][a-zA-Z0-9_]*'
    }
    ruleset('rulesets/security.xml')
    ruleset('rulesets/serialization.xml')
    ruleset('rulesets/size.xml')
    ruleset('rulesets/unnecessary.xml')
    ruleset('rulesets/unused.xml')
}
