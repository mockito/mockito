# Copyright (c) 2007 Mockito contributors 
# This program is made available under the terms of the MIT License.

require 'find'

dirs = ["src","test"]

paths = []
excludes = [".svn"]
for dir in dirs
  Find.find(dir) do |path|
    if FileTest.directory?(path)
      if excludes.include?(File.basename(path))
        Find.prune
      else
        next
      end
    else
      paths << path
    end
  end
end

new_header = 
<<-eos
/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
eos

paths.each do |path|
  File.open(path, 'r+') do |f|   
    out = ''
    f.each { |line| out << line }
  
    header = Regexp.new('.*\*/.*package org\.', Regexp::MULTILINE)
  
    if (out =~ header)     
      out.gsub!(header, new_header + "package org.")
    else 
      out = new_header + out
    end
  
    f.pos = 0           
    f.print out
    f.truncate(f.pos)             
  end
end