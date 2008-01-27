header = <<-eos
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
eos

require 'find'

dirs = ["src","test"]

paths = []
for dir in dirs
  Find.find(dir) do |path|
    if FileTest.directory?(path)
      if File.basename(path) == '.svn'
        Find.prune
      else
        next
      end
    else
      paths << path if path !~ /\.html$/
    end
  end
end

paths.each do |path|
  File.open(path, 'r+') do |f|   
    out = ''
    f.each { |line| out << line }

    current_hdr = Regexp.new('.*\*/.*package org\.', Regexp::MULTILINE)
    
    if (out !~ current_hdr)     
      out = header + out
    else
      first_hdr = Regexp.new('.*?\*/.*?[\n\r]+?', Regexp::MULTILINE)
      out.sub!(first_hdr, header)
    end
  
    f.pos = 0           
    f.print out
    f.truncate(f.pos)             
  end
end