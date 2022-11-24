package cc.rits.membership.console.reminder.helper.table

class PropertyColumnConverter {
    Column getProperty(final String property) {
        new Column(name: property)
    }
}
