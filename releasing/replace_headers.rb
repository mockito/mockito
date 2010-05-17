header = <<-eos
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
eos

require 'find'

dirs = ["../src","../test"]

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
      paths << path if path !~ /\.html$/ and path !~ /.txt$/
    end
  end
end

paths.each do |path|
  File.open(path, 'r+') do |f|   
    out = ''
    f.each { |line| out << line }

    current_hdr = Regexp.new('.*\*/.*package org\.', Regexp::MULTILINE)
    first_hdr = Regexp.new('.*?\*/.*?[\n\r]+?', Regexp::MULTILINE)
	ignore_hdr = /NON-STANDARD LICENCE HEADER HERE - THAT'S OK/
	
	def printToFile(f, out) 
	  f.pos = 0           
	  f.print out
	  f.truncate(f.pos) 
	end
	
	if (out =~ ignore_hdr)
	  #this means we're ok with non-standard header
    elsif (out !~ current_hdr)     
	  puts "header is missing for file: #{path} - replacing"
      out = header + out
	  printToFile(f, out)
    elsif (out[first_hdr] != header)
	  puts "header is different for file: #{path} - replacing"
	  out.sub!(first_hdr, header)		    
	  printToFile(f, out)
    end	
  end
end