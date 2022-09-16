import os

for command in os.listdir("../src/com/stubfx/plugin/chatreactor/commands/impl"):
    file = open("../docs/commands/{0}.md".format(command.split(".")[0]), 'w')
    file.write("## {0}\nThis command is able to...".format(command))
