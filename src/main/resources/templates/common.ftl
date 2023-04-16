<#macro buildCopy name version><button style="padding-right: 0; border-right: 0;<#if name.getRepository()??> cursor: pointer;</#if>" onClick=""<#if name.getRepository()??><#else >disabled</#if>><image <#if name.getRepository()??>title="Copied to cliboard"</#if> src="images/copy-<#if name.getRepository()??><#else>off</#if>-icon.png" height="16" /></button></#macro>

<#macro buildApiDoc name><button style="padding-right: 0; border-right: 0;<#if name.getDocPath()??> cursor: pointer;</#if>" onClick=""<#if name.getDocPath()??><#else> disabled</#if>><image <#if name.getDocPath()??>title="Go to API documenation"</#if> src="images/docs-<#if name.getDocPath()??><#else>off</#if>-icon.png" height="16" /></button></#macro>

<#macro buildLibDoc name><button style="padding-right: 0; border-right: 0;<#if name.getDocPath()??> cursor: pointer;</#if>" onClick=""<#if name.getDocPath()??><#else> disabled</#if>><image <#if name.getDocPath()??>title="Go to Lib documenation"</#if> src="images/doc-<#if name.getDocPath()??><#else>off</#if>-icon.png" height="16" /></button></#macro>

<#macro buildVersions name route=route><button style="cursor: pointer; padding-right: 0; border-right: 0;" onClick="location.href='${route}/lib?name=${name}'"><image title="Go to versions" src="images/versions-icon.png" height="18" /></button></#macro>

<#macro buildJDocs><button style="cursor: pointer; padding-right: 0; border-right: 0;" onClick="window.open('https://github.com/amadeoas/JDocs', '_blank'); return false;"><image title="Go to JDocs in Github" src="images/jdocs-icon.png" height="18" /></button></#macro>