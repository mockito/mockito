header = <<-eos
/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
eos

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

paths.each do |path|
  File.open(path, 'r+') do |f|   
    out = ''
    f.each { |line| out << line }
  
    current_hdr = Regexp.new('.*\*/.*package org\.', Regexp::MULTILINE)
  
    if (out =~ current_hdr)     
      out.gsub!(current_hdr, header + "package org.")
    else 
      out = header + out
    end
  
    f.pos = 0           
    f.print out
    f.truncate(f.pos)             
  end
end